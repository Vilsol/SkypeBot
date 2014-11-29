package me.vilsol.skypebot.engine;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import me.vilsol.skypebot.R;
import me.vilsol.skypebot.Utils;
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

        if(command.substring(0, 1).equals("@")){
            command = command.substring(1);
        }

        String[] commandSplit = command.split(" ");

        if(commandSplit.length == 0){
            return;
        }

        for(Map.Entry<String, CommandData> s : allCommands.entrySet()){
            String match = s.getKey();
            if(!s.getValue().getParameterRegex().equals("")){
                match += " " + s.getValue().getParameterRegex();
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
                CommandData data = s.getValue();

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
                    e.printStackTrace();
                }

                try{
                    methodAccessor.invoke(null, a.toArray());
                }catch(InvocationTargetException ignore){
                }

                return;
            }
        }

        if(allCommands.containsKey(command)){
            CommandData d = allCommands.get(command);
            Command c = d.getCommand();

            String correct = command;
            if(!d.getParamaterNames().equals("")){
                correct += " " + d.getParamaterNames();
            }

            if(c.command()){
                correct = R.command + correct;
            }

            R.s("Incorrect syntax: " + correct);

            return;
        }

        if(originalCommand.substring(0, 1).equals(R.command)){
            R.s("Command '" + command + "' not found!");
        }
    }

}
