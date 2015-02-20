package io.mazenmc.skypebot.handler;

import io.mazenmc.skypebot.engine.bot.Command;

import java.util.HashMap;

public class CooldownHandler {
    private HashMap<String, Long> activeCooldowns = new HashMap<>();

    public String getCooldownLeft(String command) {
        long difference = System.currentTimeMillis() - activeCooldowns.get(command.toLowerCase());
        if (difference <= 0) {
            return null;
        } else {
            return String.valueOf((difference / 1000) % 60);
        }
    }

    public void addCooldown(String command, int time) {
        activeCooldowns.put(command.toLowerCase(), System.currentTimeMillis() + (time * 1000));
    }

    public boolean tryUseCommand(Command command) {
        if (activeCooldowns.containsKey(command.name().toLowerCase())) {
            long difference = System.currentTimeMillis() - activeCooldowns.get(command.name().toLowerCase());
            if (difference <= 0) {
                activeCooldowns.remove(command.name());
                return true;
            }
            return false;
        } else {
            addCooldown(command.name(), command.cooldown());
            return true;
        }
    }
}
    