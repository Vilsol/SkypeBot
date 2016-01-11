package io.mazenmc.skypebot.game;

import xyz.gghost.jskype.user.User;
import java.util.List;

public interface Game {
    String description();
    String name();
    GameState state();
    void setState(GameState state);
    int minPlayers();
    void setMinPlayers(int minPlayers);
    List<String> activePlayers();
    void addPlayer(User user);
    void removePlayer(String player);
    User fetchUser(String name);
    void sendToAll(String message);
    void send(String user, String message);
    void startGame();
    default void onSecond() {
    }
}
