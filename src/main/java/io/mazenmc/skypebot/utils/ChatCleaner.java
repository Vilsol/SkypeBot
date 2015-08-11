package io.mazenmc.skypebot.utils;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import io.mazenmc.skypebot.modules.General;
import io.mazenmc.skypebot.stat.MessageStatistic;
import io.mazenmc.skypebot.stat.StatisticsManager;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ChatCleaner implements Runnable {
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            Collection<MessageStatistic> stats = StatisticsManager.instance().statistics().values();

            stats.stream()
                    .filter((person) -> !person.messages().isEmpty())
                    .forEach((person) -> {
                long lastSpoken = person.messages().stream()
                        .sorted((m1, m2) -> (int) (m2.time() - m1.time())).findFirst().get().time();
                long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastSpoken);

                if (days >= 7) {
                    System.out.println("removing " + person.name() + " for inactivity, " + days + ", " + lastSpoken);
                    String name = person.name();

                    try {
                        User user = Skype.getUser(person.name());
                        name = user.getDisplayName();

                        if ("".equals(name))
                            name = user.getFullName();

                        if ("".equals(name))
                            name = person.name();
                    } catch (SkypeException ignored) {
                    }

                    Resource.sendMessages(name + " is getting kicked for inactivity for a week. RIP in peace",
                            "To honor " + name + ", let us review his statistics");

                    try {
                        General.cmdStats(null, person.name());
                    } catch (SkypeException ex) {
                        Resource.sendMessage("Couldn't review his statistics, must've been a terrible member of this chat");
                    }

                    Resource.sendMessages("RIP " + name + ", may based god have mercy on his soul and fuck his bitches.",
                            "/kick " + person.name());
                    StatisticsManager.instance().removeStat(person.name());
                }
            });

            try {
                Thread.sleep(1800000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
