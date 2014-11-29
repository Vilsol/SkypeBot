package me.vilsol.skypebot.engine;

import me.vilsol.skypebot.R;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class ModuleManager {

    private static HashMap<String, CommandData> commandData = new HashMap<>();

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
                    if(command.alias() != null && command.alias().length > 0){
                        for(String s : command.alias()){
                            commandData.put(s, data);
                        }
                    }
                }
            }
        }
    }

    public static void parseText(String command){
        String originalCommand = command;

        if(command == null){
            return;
        }

        if(command.length() < 1){
            return;
        }

        if(!command.substring(0, 1).equals(R.command)){
            return;
        }

        command = command.substring(1);
        String[] commandSplit = command.split(" ");

        if(commandSplit.length == 0){
            return;
        }

        if(!commandData.containsKey(commandSplit[0])){
            return;
        }

        CommandData data = commandData.get(commandSplit[0]);

        try{
            data.getMethod().invoke(null, originalCommand);
        }catch(IllegalAccessException | InvocationTargetException ignore){
        }
    }

}
