package io.mazenmc.skypebot.utils;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.user.User;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.modules.General;
import io.mazenmc.skypebot.stat.MessageStatistic;
import io.mazenmc.skypebot.stat.StatisticsManager;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ChatCleaner implements Runnable {
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            Collection<MessageStatistic> stats = StatisticsManager.instance().statistics().values();
            Chat chat = SkypeBot.getInstance().groupConv();

            if (chat == null)
                continue;

            List<String> personList = chat.getAllUsers().stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList());

            stats.stream()
                    .filter((person) -> !person.messages().isEmpty())
                    .filter((person) -> personList.contains(person.name()))
                    .forEach((person) -> {
                        long lastSpoken = Utils.lastSpoken(person).time();
                        long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastSpoken);

                        if (days >= 7) {
                            System.out.println("removing " + person.name() + " for inactivity, " + days + ", " + lastSpoken);
                            String name = Utils.getDisplayName(Utils.getUser(person.name()));

                            Resource.sendMessages(name + " is getting kicked for inactivity for a week. RIP in peace",
                                    "To honor " + name + ", let us review his statistics");

                            try {
                                General.cmdStats(null, person.name());
                            } catch (Exception ex) {
                                Resource.sendMessage("Couldn't review his statistics, must've been a terrible member of this chat");
                            }

                            Resource.sendMessages("RIP " + name + ", may based god have mercy on his soul and f*ck his bitches.");

                            try {
                                ((GroupChat) chat).kick(person.name());
                                StatisticsManager.instance().removeStat(person.name());
                            } catch (ConnectionException ex) {
                                Resource.sendMessages("Well... turns out we can't really get rid of him, oh well...",
                                        "damn samczsun");
                            }
                        }
                    });

            StatisticsManager.instance().saveStatistics();

            try {
                Thread.sleep(1800000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
