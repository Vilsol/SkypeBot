package me.vilsol.skypebot.modules;

import com.skype.ChatMessage;
import me.vilsol.skypebot.utils.R;
import me.vilsol.skypebot.engine.Command;
import me.vilsol.skypebot.engine.Module;

import java.util.Random;

public class Swear implements Module {

    private static SwearType[][] combinations = {{SwearType.bijv_nw, SwearType.gilles, SwearType.object},
            {SwearType.bijv_nw, SwearType.gilles, SwearType.handelend},
            {SwearType.gilles, SwearType.object},
            {SwearType.gilles, SwearType.handelend}};

    @Command(name = "swear")
    public static void cmdSwear(ChatMessage message){
        SwearType[] combination = combinations[new Random().nextInt(combinations.length)];
        String output = "";
        for(SwearType s : combination){
            if(!output.equals("")){
                output += " ";
            }

            output += s.getRandomWord();
        }

        R.s(output.toUpperCase());
    }

    public enum SwearType {
        bijv_nw(new String[] {"tossing", "bloody", "shitting", "wanking", "stinky", "raging", "dementing", "dumb", "dipping", "fucking",
                "dipping", "holy", "maiming", "cocking", "ranting", "twunting", "hairy", "spunking", "flipping", "slapping",
                "sodding", "blooming", "frigging", "sponglicking", "guzzling", "glistering", "cock wielding", "failed", "artist formally known as", "unborn",
                "pulsating", "naked", "throbbing", "lonely", "failed", "stale", "spastic", "senile", "strangely shaped", "virgin",
                "bottled", "twin-headed", "fat", "gigantic", "sticky", "prodigal", "bald", "bearded", "horse-loving", "spotty",
                "spitting", "dandy", "fritzl-admiring", "friend of a", "indeterminable", "overrated", "fingerlicking", "diaper-wearing", "leg-humping",
                "gold-digging", "mong loving", "trout-faced", "cunt rotting", "flip-flopping", "rotting", "inbred", "badly drawn", "undead", "annoying",
                "whoring", "leaking", "dripping", "racist", "slutty", "cross-eyed", "irrelevant", "mental", "rotating", "scurvy looking",
                "rambling", "gag sacking", "cunting", "wrinkled old", "dried out", "sodding", "funky", "silly", "unhuman", "bloated",
                "wanktastic", "bum-banging", "cockmunching", "animal-fondling", "stillborn", "scruffy-looking", "hard-rubbing", "rectal", "glorious", "eye-less",
                "constipated", "bastardized", "utter", "hitler's personal", "irredeemable", "complete", "enormous",
                "go suck a", "fuckfaced", "broadfaced", "titless", "son of a", "demonizing", "pigfaced", "treacherous", "retarded"}),

        gilles(new String[] {"cock", "tit", "cunt", "wank", "piss", "crap", "shit", "arse", "sperm", "nipple", "anus",
                "colon", "shaft", "dick", "poop", "semen", "slut", "suck", "earwax", "fart",
                "scrotum", "cock-tip", "tea-bag", "jizz", "cockstorm", "bunghole", "food trough", "bum",
                "butt", "shitface", "ass", "nut", "ginger", "llama", "tramp", "fudge", "vomit", "cum", "lard",
                "puke", "sphincter", "nerf", "turd", "cocksplurt", "cockthistle", "dickwhistle", "gloryhole",
                "gaylord", "spazz", "nutsack", "fuck", "spunk", "shitshark", "shitehawk", "fuckwit",
                "dipstick", "asswad", "chesticle", "clusterfuck", "douchewaffle", "retard"}),

        object(new String[] {"force", "bottom", "hole", "goatse", "testicle", "balls", "bucket", "biscuit", "stain", "boy",
                "flaps", "erection", "mange", "twat", "twunt", "mong", "spack", "diarrhea", "sod",
                "excrement", "faggot", "pirate", "asswipe", "sock", "sack", "barrel", "head", "zombie", "alien",
                "minge", "candle", "torch", "pipe", "bint", "jockey", "udder", "pig", "dog", "cockroach",
                "worm", "MILF", "sample", "infidel", "spunk-bubble", "stack", "handle", "badger", "wagon", "bandit",
                "lord", "bogle", "bollock", "tranny", "knob", "nugget", "king", "hole", "kid", "trailer", "lorry", "whale",
                "rag", "foot"}),

        handelend(new String[] {"licker", "raper", "lover", "shiner", "blender", "fucker", "assjacker", "butler", "packer", "rider",
                "wanker", "sucker", "felcher", "wiper", "experiment", "wiper", "bender", "dictator", "basher", "piper",
                "slapper", "fondler", "plonker", "bastard", "handler", "herder", "fan", "amputee", "extractor", "professor", "graduate", "voyeur"});

        private String[] data;

        private SwearType(String[] data){
            this.data = data;
        }

        public String getRandomWord(){
            return data[new Random().nextInt(data.length)];
        }

    }

}
