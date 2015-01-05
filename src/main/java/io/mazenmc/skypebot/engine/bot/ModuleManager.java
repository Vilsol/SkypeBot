package io.mazenmc.skypebot.engine.bot;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.utils.Util;
import io.mazenmc.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.reflections.Reflections;
import sun.reflect.MethodAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleManager {

    private static HashMap<String, CommandData> allCommands = new HashMap<>();
    private static HashMap<String, CommandData> commandData = new HashMap<>();

    private static void executeCommand(ChatMessage message, CommandData data, Matcher m) {
        if (data.getCommand().admin()) {
            try {
                if (Arrays.asList(Util.GROUP_ADMINS).contains(message.getSenderId())){
                    Util.sendMessage("Access Denied!");
                    return;
                }
            } catch(SkypeException ignored) {
                return;
            }
        }

        List<Object> a = new ArrayList<>();
        a.add(message);

        if (m.groupCount() > 0) {
            for (int i = 1; i <= m.groupCount(); i++) {
                String g = m.group(i);
                if (g.contains(".") && Utils.isDouble(g)) {
                    a.add(Double.parseDouble(g));
                } else if (Utils.isInteger(g)) {
                    a.add(Integer.parseInt(g));
                } else {
                    a.add(g);
                }
            }
        }

        if (a.size() < data.getMethod().getParameterCount()) {
            for (int i = a.size(); i < data.getMethod().getParameterCount(); i++) {
                if (data.getMethod().getParameters()[i].getType().equals(String.class)) {
                    a.add(null);
                } else {
                    a.add(0);
                }
            }
        }

        MethodAccessor methodAccessor = null;
        try {
            Field methodAccessorField = Method.class.getDeclaredField("methodAccessor");
            methodAccessorField.setAccessible(true);
            methodAccessor = (MethodAccessor) methodAccessorField.get(data.getMethod());

            if (methodAccessor == null) {
                Method acquireMethodAccessorMethod = Method.class.getDeclaredMethod("acquireMethodAccessor", null);
                acquireMethodAccessorMethod.setAccessible(true);
                methodAccessor = (MethodAccessor) acquireMethodAccessorMethod.invoke(data.getMethod(), null);
            }
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            Util.sendMessage("Failed... (" + ExceptionUtils.getStackTrace(e) + ")");
        }

        try {
            methodAccessor.invoke(null, a.toArray());
        } catch (Exception e) {
            Util.sendMessage("Failed... (" + Utils.upload(ExceptionUtils.getStackTrace(e)) + ")");
        }

    }

    public static HashMap<String, CommandData> getCommands() {
        return commandData;
    }

    public static void loadModules(String modulePackage) {
        Reflections r = new Reflections(modulePackage);
        Set<Class<? extends Module>> classes = r.getSubTypesOf(Module.class);

        for (Class<? extends Module> c : classes) {
            for (Method m : c.getMethods()) {
                Command command;
                command = m.getAnnotation(Command.class);

                if (command != null) {
                    CommandData data = new CommandData(command, m);

                    System.out.println("registered " + command.name());

                    commandData.put(command.name(), data);
                    allCommands.put(command.name(), data);
                    if (command.alias() != null && command.alias().length > 0) {
                        for (String s : command.alias()) {
                            allCommands.put(s, data);
                        }
                    }
                }
            }
        }
    }

    public static void parseText(ChatMessage message) {
        String command = null;
        String originalCommand = null;
        try {
            command = message.getContent();
            originalCommand = message.getContent();
        } catch (SkypeException ignored) {
            System.out.println("skype exception");
            return;
        }

        if (command == null) {
            System.out.println("command is null");
            return;
        }

        System.out.println("got message: " + command);

        if (command.length() < 1) {
            System.out.println("low command length");
            return;
        }

        if (command.startsWith(Util.COMMAND_PREFIX)) {
            command = command.substring(1);
        }

        String[] commandSplit = command.split(" ");

        if (commandSplit.length == 0) {
            System.out.println("nothing");
            return;
        }

        for (Map.Entry<String, CommandData> s : allCommands.entrySet()) {
            String match = s.getKey();
            if (!s.getValue().getParameterRegex(false).equals("")) {
                match += " " + s.getValue().getParameterRegex(false);
            }

            if (s.getValue().getCommand().command()) {
                match = Util.COMMAND_PREFIX + match;
            }

            if (s.getValue().getCommand().exact()) {
                match = "^" + match + "$";
            }

            Pattern r = Pattern.compile(match);
            Matcher m = r.matcher(originalCommand);

            if (m.find()) {
                executeCommand(message, s.getValue(), m);
                System.out.println("executed command");
                return;
            } else if (!s.getValue().getParameterRegex(false).equals(s.getValue().getParameterRegex(true))) {
                match = s.getKey();
                if (!s.getValue().getParameterRegex(true).equals("")) {
                    match += " " + s.getValue().getParameterRegex(true);
                }

                if (s.getValue().getCommand().command()) {
                    match = Util.COMMAND_PREFIX + match;
                }

                if (s.getValue().getCommand().exact()) {
                    match = "^" + match + "$";
                }

                r = Pattern.compile(match);
                m = r.matcher(originalCommand);
                if (m.find()) {
                    executeCommand(message, s.getValue(), m);
                    return;
                }
            }
        }

        if (allCommands.containsKey(commandSplit[0])) {
            CommandData d = allCommands.get(commandSplit[0]);
            Command c = d.getCommand();

            String correct = commandSplit[0];
            if (!d.getParamaterNames().equals("")) {
                correct += " " + d.getParamaterNames();
            }

            if (c.command()) {
                if (!originalCommand.startsWith("@")) {
                    return;
                }

                correct = Util.COMMAND_PREFIX + correct;
            }

            Util.sendMessage("Incorrect syntax: " + correct);

            return;
        }

        if (originalCommand.startsWith(Util.COMMAND_PREFIX)) {
            Util.sendMessage("Command '" + commandSplit[0] + "' not found!");
        }
    }

}
