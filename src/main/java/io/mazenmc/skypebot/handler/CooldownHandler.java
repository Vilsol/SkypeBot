package io.mazenmc.skypebot.handler;

import io.mazenmc.skypebot.engine.bot.Command;

import java.util.HashMap;
import java.util.Map;

public class CooldownHandler {
    private Map<String, Long> activeCooldowns = new HashMap<>();

    public String getCooldownLeft(String command) {
        long difference = activeCooldowns.get(command.toLowerCase()) - System.currentTimeMillis();
        if (difference <= 0) {
            return null;
        } else {
            return String.valueOf((difference / 1000) % 60);
        }
    }

    public void addCooldown(String command, int seconds) {
        activeCooldowns.put(command.toLowerCase(), System.currentTimeMillis() + (seconds * 1000));
    }

    /**
     * @deprecated the name would suggest it executes the command
     */
    @Deprecated
    public boolean tryUseCommand(Command command) {
        return canUse(command);
    }
    
    public boolean canUse(Command command) {
        Long timestamp = activeCooldowns.get(command.name().toLowerCase());
        long current = System.currentTimeMillis();
        boolean canUse = timestamp == null || current > timestamp;
        if(canUse) {
            addCooldown(command.name(), command.cooldown());
        }
        return canUse;
    }
}
    
