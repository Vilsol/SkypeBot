package io.mazenmc.skypebot.modules

import com.samczsun.skype4j.chat.messages.ReceivedMessage
import io.mazenmc.skypebot.engine.bot.CommandBuilder
import io.mazenmc.skypebot.engine.bot.ModuleManager
import io.mazenmc.skypebot.engine.bot.generic.*
import io.mazenmc.skypebot.utils.CommandResources
import io.mazenmc.skypebot.utils.Utils
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Predicate

public object General {
    public fun load() {
        ModuleManager.registerCommand(CommandBuilder("choice"), MethodResponse(Responder() { m, a -> choice(m, a)}))
        ModuleManager.registerCommand(CommandBuilder("8ball"), StringResponse("[ar.options]")
                .addArray("options", CommandResources.ballResponses))

        ModuleManager.registerCommand(CommandBuilder("flickr.com/photos/stuntguy3000").exact(false).command(false),
                StringResponse("Nobody likes your photos, Luke.").predicate(CommandResources.flickrPredicate))
        ModuleManager.registerCommand(CommandBuilder("(?i)savage").command(false).alias(arrayOf("(?i)brutal")),
                StringResponse("[ar.links]").addArray("links", CommandResources.savageLinks))

        ModuleManager.registerCommand(CommandBuilder("lenny"), StringResponse("( ͡° ͜ʖ ͡°)"))
        ModuleManager.registerCommand(CommandBuilder("flip"), StringResponse("(╯°□°）╯︵ ┻━┻)"))
        ModuleManager.registerCommand(CommandBuilder("idk"), StringResponse("¯\\_(ツ)_/¯"))
        ModuleManager.registerCommand(CommandBuilder("wat"), StringResponse("http://waitw.at O.o"))
        ModuleManager.registerCommand(CommandBuilder("basoon"), StringResponse("Blows Air Strongly Out Of Nose"))
        ModuleManager.registerCommand(CommandBuilder("md5"), StringResponse(CommandResources.md5Response))
        ModuleManager.registerCommand(CommandBuilder("thank mr bot").command(false).exact(false),
                StringResponse("may good cpus and dedotated wams come to you"))
        ModuleManager.registerCommand(CommandBuilder("git"),
                StringResponse("Git Repository: https://github.com/mkotb/SkypeBot"))

        ModuleManager.registerCommand(CommandBuilder("dreamincode").alias(arrayOf("whatwouldmazensay")),
                StringResponse("[ar.quotes]").addArray("quotes", CommandResources.codeDreams))
        ModuleManager.registerCommand(CommandBuilder("roflcopter").alias(arrayOf("rofl")),
                StringResponse("ROFL all day long! http://goo.gl/pCIqXv"))
        ModuleManager.registerCommand(CommandBuilder("confirmed"),
                StringResponse("[ar.responses]").addArray("responses", CommandResources.confirmed))
        ModuleManager.registerCommand(CommandBuilder("ayy").command(false), StringResponse("lmao"))
        ModuleManager.registerCommand(CommandBuilder("restart"), ProcessedResponse("Restarting",
                CommandProcessor() { m, a -> Utils.restartBot()}))

    }

    private fun choice(message: ReceivedMessage, args: Array<out String>): String {
        var choices = Utils.compiledArgs(message).splitBy(",")

        if (choices.size() <= 1) {
            return "Give me choices!"
        }

        return "I say " + choices[ThreadLocalRandom.current().nextInt(choices.size())]
    }
