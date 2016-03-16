package io.mazenmc.skypebot

fun main(args: Array<String>) {
    SkypeBot.loadThirdParty()
    SkypeBot.loadConfig()
    SkypeBot.loadSkype()
}