package io.mazenmc.skypebot.handler;

import io.mazenmc.skypebot.engine.bot.Command;

import java.util.HashMap;
import java.util.Map;

public class CooldownHandler extends Thread {
    private HashMap<String, Integer> activeCooldowns = new HashMap<>();

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                return;
            }

            for (Map.Entry<String, Integer> commandCooldown : new HashMap<>(activeCooldowns).entrySet()) {
                int timeLeft = activeCooldowns.get(commandCooldown.getKey()) - 1;
                if (timeLeft <= 1) {
                    activeCooldowns.remove(commandCooldown.getKey());
                } else {
                    activeCooldowns.put(commandCooldown.getKey(), timeLeft);
                }
            }
        }
    }

    public int getCooldownLeft(String command) {
        return activeCooldowns.get(command.toLowerCase());
    }

    public void addCooldown(String command, int time) {
        activeCooldowns.put(command.toLowerCase(), time);
    }

    public boolean tryUseCommand(Command command) {
        if (activeCooldowns.containsKey(command.name())) {
            return false;
        } else {
            addCooldown(command.name(), command.cooldown());
            return true;
        }
    }
}
    