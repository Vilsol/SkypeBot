package io.mazenmc.skypebot.utils

import com.samczsun.skype4j.chat.messages.ReceivedMessage
import java.util.function.Predicate

public object CommandResources {
    public val weatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?units=imperial&q="
    public val ballResponses: Array<String> = arrayOf("It is certain", "It is decidedly so", "Without a doubt",
                                                      "Yes definitely", "You may rely on it", "As I see it, yes",
                                                      "Most likely", "Outlook good", "Yes", "Signs point to yes",
                                                      "Reply hazy try again", "Ask again later", "Better not tell you now",
                                                      "Cannot predict now", "Concentrate and ask again", "Don't count on it",
                                                      "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful")
    public val flickrPredicate: Predicate<ReceivedMessage> = Predicate() {it.getSender().getUsername().equals("stuntguy3000")}
    public val savageLinks: Array<String> = arrayOf("https://i.imgur.com/t137FTZ.jpg", "https://i.imgur.com/jLLRs2j.jpg",
                                                    "https://i.imgur.com/sES5tg1.png", "https://i.imgur.com/kHQUtvf.jpg",
                                                    "https://i.imgur.com/VaHSXD4.png", "https://i.imgur.com/sDdHFWp.png",
                                                    "https://i.imgur.com/KOCnBca.gif")
    public val codeDreams: Array<String> = arrayOf(
            "No, I'm not interested in having a girlfriend I find it a tremendous waste of time.",
            "Hi, my name is Santiago Gonzalez and I'm 14 and I like to program.",
            "I'm fluent in a dozen different programming languages.",
            "Thousands of people have downloaded my apps for the Mac, iPhone, and iPad.",
            "I will be 16 when I graduate college and 17 when I finish my masters.",
            "I really like learning, I find it as essential as eating.",
            "Dr. Bakos: I often have this disease which I call long line-itus.",
            "Dr. Bakos: Are you eager enough just to write down a slump of code, or is the code itself a artistic medium?",
            "Beautiful code is short and concise.",
            "When you're around a certain race enough, you tend to become like them physically and mentally; " +
                    "soon enough I'll be driving terribly and doing math competitions",
            "Sometimes when I go to sleep I'm stuck with that annoying bug I cannot fix, and in my dreams I see myself programming. " +
                    "\nWhen I wake up I have the solution!",
            "One of the main reasons I started developing apps was to help people what they want to do like decorate a christmas tree.",
            "I really like to crochet.",
            "I made a good website http://slgonzalez.com/",
            "If that was my sister I'd eat her.",
            "https://s.mzn.pw/08bG9Ti.png",
            "ISIS more like waswas",
            "eat less food habibi @aaomidi",
            "Are aero bars forming in my ears?",
            "yo I was walking down the halls and into my next class playing \"In Da Club\" by 50 cent on full volume with my laptop on one hand",
            "Less halawa more carrot @aaomidi",
            "I live in Vancouver, which is practically Asia. Soon enough British Columbia will be renamed to Asian Columbia")
    public val confirmed: Array<String> = arrayOf("[arg.1] Confirmed", "[arg.1] won't happen!" ,"[arg.1] will happen some day",
            "[arg.1] will happen some day", "Just wait and see")
    public val md5Response: String = "1% of devs (people who know their shit)\n" +
            "md_2 = uses one class for everything\n" +
            "md_3 = true == true, yoo!\n" +
            "md_4 = New instance to call static methods\n" +
            "md_5 = reflects his own classes\n" +
            "md_6 = return this; on everything\n" +
            "md_7 = abstract? never heard of it\n" +
            "md_8 = interface? never heard of it\n" +
            "md_9 = enum? never heard of it\n" +
            "md_10 = java? never heard of it"
    public val loveCards: Array<String> = arrayOf("https://i.imgur.com/4WAW8vM.jpg",
            "https://i.imgur.com/8zMPZYa.png", "https://i.imgur.com/w4JdQFP.jpg",
            "https://i.imgur.com/nymmBgA.jpg", "https://i.imgur.com/J0E0SHS.jpg",
            "https://i.imgur.com/G7nAIgU.jpg", "https://i.imgur.com/qsfSjK0.jpg",
            "https://i.imgur.com/V1D4Ku8.jpg", "https://i.imgur.com/jO5baBM.png",
            "https://i.imgur.com/LXiQo1g.png", "https://i.imgur.com/Hj9VDIx.jpg",
            "https://i.imgur.com/88F1rR6.jpg", "https://i.imgur.com/zM3wXsv.jpg",
            "https://i.imgur.com/kUqruhJ.jpg", "https://i.imgur.com/wf7t0ad.jpg",
            "https://i.imgur.com/WBZI9yS.jpg")
    public val justisQuotes: Array<String> = arrayOf("Guys, Can confirm. Penis exersizes DO work.",
            "It's only been a week and there is a noticable difference.",
            "Excersizing my phallus.",
            "Any lady of mine is gunna feel real lucky.",
            "If only you guys were as passionate about giving your women pleasure.",
            "FYI, I have plenty of ladies.",
            "Any women will apreciate your effort. Knowing you care.",
            "I'm good at what I do. ;)",
            "I expect lots of cake!",
            "We could be sex buddies!!! Makin porn together!!",
            "All our sex toys are made from 100% ultra-premium custom forumulated silicone; garenteed to last a lifetime.",
            "Easy To Clean, Eco-Friendly, Hypoallergenic, Hygienic, Boilable, Bleachable and Dishwasher Safe. ;D",
            "Just in case you ever wanted to wash your vibrator with your eating utensils. I know I do.",
            "I think I've been deepthroating too much... It hurts so much to swallow... ;-;")
}