package me.vilsol.skypebot.engine;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import me.vilsol.skypebot.R;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    public static void parseText(ChatMessage message) throws SkypeException{
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
            if(!s.getValue().getCommand().parameters().equals("")){
                match += " " + s.getValue().getCommand().parameters();
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
                    if(!Arrays.asList(data.getCommand().allow()).contains(message.getSenderId())){
                        R.s("Access Denied!");
                        return;
                    }
                }

                try{
                    data.getMethod().invoke(null, message);
                }catch(IllegalAccessException | InvocationTargetException ignore){
                }

                return;
            }
        }

        if(allCommands.containsKey(command)){
            Command c = allCommands.get(command).getCommand();

            String correct = command;
            if(!c.parameters().equals("")){
                correct += " " + c.parameters();
            }

            if(c.command()){
                correct = R.command + correct;
            }

            if(c.exact()){
                correct = "^" + correct + "$";
            }

            R.s("Incorrect syntax: " + correct);

            return;
        }

        if(originalCommand.substring(0, 1).equals(R.command)){
            R.s("Command '" + command + "' not found!");
        }
    }

}
