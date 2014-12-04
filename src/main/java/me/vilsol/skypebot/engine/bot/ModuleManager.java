package me.vilsol.skypebot.engine.bot;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import me.vilsol.skypebot.utils.R;
import me.vilsol.skypebot.utils.Utils;
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

    private static HashMap<String, CommandData> commandData = new HashMap<>();
    private static HashMap<String, CommandData> allCommands = new HashMap<>();

    public static void loadModules(String modulePackage){
        Reflections r = new Reflections(modulePackage);
        Set<Class<? extends Module>> classes = r.getSubTypesOf(Module.class);
        for(Class<? extends Module> c : classes){
            for(Method m : c.getMethods()){
                Command command;
                command = m.getAnnotation(Command.class);
                if(command != null){
                    CommandData data = new CommandData(command, m);
                    commandData.put(command.name(), data);
                    allCommands.put(command.name(), data);
                    if(command.alias() != null && command.alias().length > 0){
                        for(String s : command.alias()){
                            allCommands.put(s, data);
                        }
                    }
                }
            }
        }
    }

    private static void executeCommand(ChatMessage message, CommandData data, Matcher m){

        if(data.getCommand().allow() != null && data.getCommand().allow().length > 0){
            try{
                if(!Arrays.asList(data.getCommand().allow()).contains(message.getSenderId())){
                    R.s("Access Denied!");
                    return;
                }
            }catch(SkypeException ignored){
                return;
            }
        }

        List<Object> a = new ArrayList<>();
        a.add(message);

        if(m.groupCount() > 0){
            for(int i = 1; i <= m.groupCount(); i++){
                String g = m.group(i);
                if(g.contains(".") && Utils.isDouble(g)){
                    a.add(Double.parseDouble(g));
                }else if(Utils.isInteger(g)){
                    a.add(Integer.parseInt(g));
                }else{
                    a.add(g);
                }
            }
        }

        if(a.size() < data.getMethod().getParameterCount()){
            for(int i = a.size(); i < data.getMethod().getParameterCount(); i++){
                if(data.getMethod().getParameters()[i].getType().equals(String.class)){
                    a.add(null);
                }else{
                    a.add(0);
                }
            }
        }

        MethodAccessor methodAccessor = null;
        try{
            Field methodAccessorField = Method.class.getDeclaredField("methodAccessor");
            methodAccessorField.setAccessible(true);
            methodAccessor = (MethodAccessor) methodAccessorField.get(data.getMethod());

            if(methodAccessor == null){
                Method acquireMethodAccessorMethod = Method.class.getDeclaredMethod("acquireMethodAccessor", null);
                acquireMethodAccessorMethod.setAccessible(true);
                methodAccessor = (MethodAccessor) acquireMethodAccessorMethod.invoke(data.getMethod(), null);
            }
        }catch(NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e){
            R.s("Failed... (" + ExceptionUtils.getStackTrace(e) + ")");
        }

        try{
            methodAccessor.invoke(null, a.toArray());
        }catch(Exception e){
            R.s("Failed... (" + Utils.upload(ExceptionUtils.getStackTrace(e)) + ")");
        }

    }

    public static void parseText(ChatMessage message){
        String command = null;
        String originalCommand = null;
        try{
            command = message.getContent();
            originalCommand = message.getContent();
        }catch(SkypeException ignored){
        }

        if(command == null){
            return;
        }

        if(command.length() < 1){
            return;
        }

        if(command.startsWith(R.command)){
            command = command.substring(1);
        }

        String[] commandSplit = command.split(" ");

        if(commandSplit.length == 0){
            return;
        }

        for(Map.Entry<String, CommandData> s : allCommands.entrySet()){
            String match = s.getKey();
            if(!s.getValue().getParameterRegex(false).equals("")){
                match += " " + s.getValue().getParameterRegex(false);
            }

            if(s.getValue().getCommand().command()){
                match = R.command + match;
            }

            if(s.getValue().getCommand().exact()){
                match = "^" + match + "$";
            }

            Pattern r = Pattern.compile(match);
            Matcher m = r.matcher(originalCommand);

            if(m.find()){
                executeCommand(message, s.getValue(), m);
                return;
            }else if(!s.getValue().getParameterRegex(false).equals(s.getValue().getParameterRegex(true))){
                match = s.getKey();
                if(!s.getValue().getParameterRegex(true).equals("")){
                    match += " " + s.getValue().getParameterRegex(true);
                }

                if(s.getValue().getCommand().command()){
                    match = R.command + match;
                }

                if(s.getValue().getCommand().exact()){
                    match = "^" + match + "$";
                }

                r = Pattern.compile(match);
                m = r.matcher(originalCommand);
                if(m.find()){
                    executeCommand(message, s.getValue(), m);
                    return;
                }
            }
        }

        if(allCommands.containsKey(commandSplit[0])){
            CommandData d = allCommands.get(commandSplit[0]);
            Command c = d.getCommand();

            String correct = commandSplit[0];
            if(!d.getParamaterNames().equals("")){
                correct += " " + d.getParamaterNames();
            }

            if(c.command()){
                if(!originalCommand.startsWith("@")){
                    return;
                }

                correct = R.command + correct;
            }

            R.s("Incorrect syntax: " + correct);

            return;
        }

        if(originalCommand.startsWith(R.command)){
            R.s("Command '" + commandSplit[0] + "' not found!");
        }
    }

    public static HashMap<String, CommandData> getCommands(){
        return commandData;
    }

}
