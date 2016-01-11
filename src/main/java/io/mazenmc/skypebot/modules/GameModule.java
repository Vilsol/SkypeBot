package io.mazenmc.skypebot.modules;

import xyz.gghost.jskype.message.Message;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.game.Game;
import io.mazenmc.skypebot.game.GameManager;
import io.mazenmc.skypebot.utils.Resource;

public class GameModule implements Module {
    @Command(name = "startgame")
    public static void startGame(Message message, String name) throws Exception {
        Game game = GameManager.instance().startGame(name);

        if (game == null) {
            Resource.sendMessage(message, "No game type with the name " + name + " was found");
            return;
        }

        Resource.sendMessage(message, game.name() + " was started! Sending description...");
        Resource.sendMessage(message, game.description());
    }

    @Command(name = "forceend", admin = true)
    public static void endGame(Message message) {
        Game current = GameManager.instance().current();

        if (current == null) {
            Resource.sendMessage(message, "There is no game in session. Cannot stop non-existent game");
            return;
        }

        Resource.sendMessage(message, current.name() + " was forced stopped! Thanks for playing");
        GameManager.instance().stopGame();
    }
}
