package io.mazenmc.skypebot.modules

import com.samczsun.skype4j.chat.messages.ReceivedMessage
import io.mazenmc.skypebot.SkypeBot
import io.mazenmc.skypebot.engine.bot.CommandBuilder
import io.mazenmc.skypebot.engine.bot.ModuleManager
import io.mazenmc.skypebot.engine.bot.generic.*
import io.mazenmc.skypebot.stat.Message
import io.mazenmc.skypebot.stat.StatisticsManager
import io.mazenmc.skypebot.utils.CommandResources
import io.mazenmc.skypebot.utils.Resource
import io.mazenmc.skypebot.utils.Utils
import java.net.URLEncoder
import java.sql.Date
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Predicate

public object General {
    public fun load() {
        ModuleManager.registerCommand(CommandBuilder("c"),
                MethodResponse(Responder() { m, a -> Resource.askQuestion(Utils.compiledArgs(m))}))
        ModuleManager.registerCommand(CommandBuilder("lmgtfy"),
                MethodResponse(Responder { m, a ->  "http://lmgtfy.com/?q=${URLEncoder.encode(Utils.compiledArgs(m))}"}))
        ModuleManager.registerCommand(CommandBuilder("choice"), MethodResponse(Responder() { m, a -> choice(m, a)}))
        ModuleManager.registerCommand(CommandBuilder("exclude").min(2), MethodResponse(Responder() { m, a -> exclude(m, a)}))
        ModuleManager.registerCommand(CommandBuilder("kick").admin(true).min(1), MethodResponse(Responder { m, a -> kick(a)}))
        ModuleManager.registerCommand(CommandBuilder("loveme"), MethodResponse(Responder { m, a -> loveMe(m) }))
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
        ModuleManager.registerCommand(CommandBuilder("hof"),
                StringResponse("https://gist.github.com/mkotb/3befd5bf719496278052"))

        ModuleManager.registerCommand(CommandBuilder("dreamincode").alias(arrayOf("whatwouldmazensay")),
                StringResponse("[ar.quotes]").addArray("quotes", CommandResources.codeDreams))
        ModuleManager.registerCommand(CommandBuilder("whatwouldjustissay").alias(arrayOf("phallusexersize")),
                StringResponse("[ar.quotes]").addArray("quotes", CommandResources.justisQuotes))
        ModuleManager.registerCommand(CommandBuilder("roflcopter").alias(arrayOf("rofl")),
                StringResponse("ROFL all day long! http://goo.gl/pCIqXv"))
        ModuleManager.registerCommand(CommandBuilder("confirmed"),
                StringResponse("[ar.responses]").addArray("responses", CommandResources.confirmed))
        ModuleManager.registerCommand(CommandBuilder("ayy").command(false), StringResponse("lmao"))
        ModuleManager.registerCommand(CommandBuilder("restart"), ProcessedResponse("Restarting...",
                CommandProcessor() { m, a -> Utils.restartBot()}))
        ModuleManager.registerCommand(CommandBuilder("restoretopic"), ProcessedResponse("N/A",
                CommandProcessor { m, a -> SkypeBot.groupConv()?.setTopic("Mazen's Skype Chat") }))
        ModuleManager.registerCommand(CommandBuilder("pme"), ProcessedResponse("N/A",
                CommandProcessor { m, a -> m.getSender().getContact()
                        .getPrivateConversation().sendMessage(Utils.compiledArgs(m)) }))
    }

    private fun choice(message: ReceivedMessage, args: Array<out String>): String {
        var choices = Utils.compiledArgs(message).splitBy(",")

        if (choices.size() <= 1) {
            return "Give me choices!"
        }

        return "I say " + choices[ThreadLocalRandom.current().nextInt(choices.size())]
    }

    private fun exclude(message: ReceivedMessage, args: Array<out String>): String {
        var stat = StatisticsManager.statistics().get(args[1]) ?: return "Couldn't find statistic by " + args[1]

        if (!Utils.isInteger(args[0]))
            return "Please provide an integer for the amount of days you wish to exclude this individual"

        var date = Date(System.currentTimeMillis() + (Integer.parseInt(args[0]) * 86400000L))

        stat.addMessage(Message("Gone until ${date}", date.getTime()))
        return "Successfully excluded ${args[1]} from chat cleaner until ${date}"
    }

    private fun kick(args: Array<out String>): String {
        var stat = StatisticsManager.statistics().get(args[0]) ?: return "Couldn't find statistic by " + args[0]

        StatisticsManager.removeStat(args[0])
        SkypeBot.groupConv()?.kick(args[0])
        return "Kicked ${args[0]} and removed their stats"
    }

    private fun loveMe(message: ReceivedMessage): String {
        var contact = message.getSender().getContact()
        var rand = ThreadLocalRandom.current().nextInt(CommandResources.loveCards.size())

        contact.sendRequest("I love you")
        contact.getPrivateConversation().sendMessage("You can be my special friend <3")
        contact.getPrivateConversation().sendMessage(CommandResources.loveCards[rand])
        return "I'll love you... :*"
    }
}