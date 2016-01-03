package io.mazenmc.skypebot.game;

import io.mazenmc.skypebot.game.cah.CardsAgainstHumanity;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();
    private Game current;
    private List<Class<? extends Game>> registeredGames = new ArrayList<>();

    private GameManager() {
        registeredGames.add(CardsAgainstHumanity.class);
    }

    public static GameManager instance() {
        return INSTANCE;
    }

    public <T extends Game> T startGame(String name) throws InstantiationException, IllegalAccessException {
        Class<? extends Game> belongingClass = findGame(name);

        if (belongingClass == null) {
            return null;
        }

        current = belongingClass.newInstance();

        return (T) current;
    }

    public void stopGame() {
        if (current != null) {
            current.setState(GameState.ENDED);
        }

        current = null;
        Resource.sendMessage("Game has been stopped! Thanks for playing!");
    }

    public Game current() {
        return current;
    }

    private Class<? extends Game> findGame(String name) {
        return Utils.optionalGet(registeredGames.stream().filter((c) -> c.getName().equalsIgnoreCase(name)).findFirst());
    }
}
