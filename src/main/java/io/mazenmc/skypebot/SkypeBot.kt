package io.mazenmc.skypebot

import com.samczsun.skype4j.Skype
import com.samczsun.skype4j.SkypeBuilder
import com.samczsun.skype4j.chat.Chat
import com.samczsun.skype4j.exceptions.ConnectionException
import com.samczsun.skype4j.exceptions.handler.ErrorHandler
import com.samczsun.skype4j.exceptions.handler.ErrorSource
import com.samczsun.skype4j.formatting.Message
import com.samczsun.skype4j.formatting.Text
import com.samczsun.skype4j.internal.SkypeEventDispatcher
import io.mazenmc.skypebot.utils.Resource
import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

public object SkypeBot {
    private val scheduler:     ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val relogRunnable: Runnable                 = RelogRunnable()
    private val errorHandler:  ErrorHandler             = BotErrHandler()

    private var username:  String? = null
    private var password:  String? = null
    private var groupConv: Chat?   = null
    private var skype:     Skype?  = null

    private var listenerMap:  Field        by Delegates.notNull()

    public fun loadConfig() {
        var prop = Properties()
        var config = File("bot.properties")

        if (!config.exists()) {
            FileOutputStream(config).use {
                prop.setProperty("username", "your.skype.username")
                prop.setProperty("password", "your.skype.password")
                prop.store(it, null)
            }
            println("Generated default configuration. Exiting")
            System.exit(1)
            return
        }

        FileInputStream(config).use {
            prop.load(it)
            username = prop.getProperty("username")
            password = prop.getProperty("password")
        }
    }

    public fun loadSkype() {
        try {
            listenerMap = SkypeEventDispatcher::class.javaClass.getDeclaredField("listeners")
            listenerMap.setAccessible(true)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace() // welp rip
            System.exit(0)
            return
        }

        scheduler.scheduleAtFixedRate(relogRunnable, 0, 8, TimeUnit.HOURS)
    }

    public fun groupConv(): Chat? {
        if (groupConv == null) {
            groupConv = skype?.getOrLoadChat("19:7cb2a86653594e18abb707e03e2d1848@thread.skype")
        }

        return groupConv
    }

    public fun getSkype(): Skype? {
        return skype
    }

    public fun sendMessage(message: String) {
        try {
            groupConv()?.sendMessage(message)
        } catch (e: ConnectionException) {
            groupConv = null
            sendMessage(message)
        }
    }

    public fun sendMessage(message: Message) {
        try {
            groupConv()?.sendMessage(message)
        } catch (e: ConnectionException) {
            groupConv = null
            sendMessage(message)
        }
    }

    private class RelogRunnable: Runnable {
        override fun run() {
            println("Starting relog process")
            var newSkype: Skype? = null
            var retry = true
            while (retry) {
                try {
                    newSkype = SkypeBuilder(username, password).withAllResources()
                            .withExceptionHandler(errorHandler).build()
                    newSkype.login()
                    println("Logged in with username ${username}")
                    newSkype.subscribe()
                    println("Successfully subscribed")
                    newSkype.getEventDispatcher().registerListener(SkypeEventListener())
                    retry = false
                } catch (t: Throwable) {
                    t.printStackTrace()
                    Thread.sleep(10000)
                }
            }

            groupConv = null
            var oldSkype = skype
            skype = newSkype

            if (oldSkype != null) {
                try {
                    var listeners = listenerMap.get(oldSkype.getEventDispatcher())

                    if (listeners is MutableMap<*, *>) {
                        listeners.clear()
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                println("Logging out of old Skype")
                oldSkype.logout()
            } else {
                sendMessage(Message.create().with(Text.rich("Mazen's Bot ${Resource.VERSION} started!").withColor(Color.GREEN)))
            }
        }
    }

    private class BotErrHandler: ErrorHandler {
        override fun handle(errorSource: ErrorSource?, error: Throwable?, shutdown: Boolean) {
            if (shutdown) {
                println("Error detected, relogging: ${error}")
                skype = null
                scheduler.submit(relogRunnable)
            }
        }
    }
}