package io.mazenmc.skypebot.game;

import io.mazenmc.skypebot.SkypeBot;
import xyz.gghost.jskype.user.User;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseGame implements Game {
    private GameState state = GameState.WAITING_FOR_PLAYERS;
    private int minPlayers = Integer.MAX_VALUE;
    private List<String> activePlayers = new ArrayList<>();
    protected Map<String, Integer> scores = new HashMap<>();

    @Override
    public GameState state() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        if (state == GameState.ENDED) {
            end();
        }

        this.state = state;
    }

    @Override
    public int minPlayers() {
        return minPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public void addPlayer(User user) {
        activePlayers.add(user.getUsername());
        scores.put(user.getUsername(), 0);
    }

    @Override
    public void removePlayer(String user) {
        activePlayers.remove(user);
        scores.remove(user);
    }

    public boolean incrementScore(String user) {
        if (!activePlayers.contains(user)) {
            return false;
        }

        if (!scores.containsKey(user)) {
            scores.put(user, 1);
            return true;
        }

        scores.put(user, scores.get(user) + 1);
        return true;
    }

    public void setScore(String user, int score) {
        if (!activePlayers.contains(user)) {
            return;
        }

        this.scores.put(user, score);
    }

    public int scoreFor(String user) {
        if (!activePlayers.contains(user)) {
            return -1;
        }

        if (!scores.containsKey(user)) {
            return 0;
        }

        return scores.get(user);
    }

    @Override
    public List<String> activePlayers() {
        return activePlayers;
    }

    @Override
    public void send(String user, String message) {
        try {
            fetchUser(user).getGroup(SkypeBot.getInstance().getSkype()).sendMessage(message);
        } catch (Exception e) {
            Resource.sendMessage("Failed to send a message to " + user + "...");
        }
    }

    @Override
    public User fetchUser(String name) {
        return Utils.getUser(name);
    }

    @Override
    public void sendToAll(String message) {
        Resource.sendMessage(name() + ": " + message);
    }

    public void end() {}
}
