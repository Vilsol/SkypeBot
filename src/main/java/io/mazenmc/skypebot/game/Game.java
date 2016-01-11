package io.mazenmc.skypebot.game;

import in.kyle.ezskypeezlife.api.obj.SkypeUser;

import java.util.List;

public interface Game {
    String description();
    String name();
    GameState state();
    void setState(GameState state);
    int minPlayers();
    void setMinPlayers(int minPlayers);
    List<String> activePlayers();
    void addPlayer(SkypeUser user);
    void removePlayer(String player);
    SkypeUser fetchUser(String name);
    void sendToAll(String message);
    void send(String user, String message);
    void startGame();
    default void onSecond() {
    }
}
