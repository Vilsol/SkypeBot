package io.mazenmc.skypebot.handler;

import io.mazenmc.skypebot.engine.bot.Command;

import java.util.HashMap;

public class CooldownHandler {
    private HashMap<String, Long> activeCooldowns = new HashMap<>();

    public String getCooldownLeft(String command) {
        long difference = activeCooldowns.get(command.toLowerCase()) - System.currentTimeMillis();
        if (difference <= 0) {
            return null;
        } else {
            return String.valueOf((difference / 1000) % 60);
        }
    }

    public void addCooldown(String command, long time) {
        activeCooldowns.put(command.toLowerCase(), time);
    }

    public boolean tryUseCommand(Command command) {
        Long timestamp = activeCooldowns.get(command.name().toLowerCase());
        long current = System.currentTimeMillis();
        boolean cooling = timestamp == null || current > timestamp;
        if(!cooling) {
            addCooldown(command.name(), current + (1000 * command.cooldown()));
        }
        return cooling;
    }
}
    
