package me.vilsol.skypebot.engine;

import com.google.common.base.Joiner;
import me.vilsol.skypebot.R;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;

public class CommandData {

    private Command command;
    private Method method;
    private HashMap<String, String> parameters = new HashMap<>();

    public CommandData(Command c, Method method){
        this.command = c;
        this.method = method;

        System.out.println(Arrays.asList(method.getParameters()));

        if(method.getParameterCount() > 1){
            int param = 1;
            for(Parameter p : method.getParameters()){
                if(param == 1){
                    param++;
                    continue;
                }

                if(p.getType().equals(Integer.class)){
                    parameters.put(p.getName(), R.REGEX_INT);
                }else if(p.getType().equals(Double.class)){
                    parameters.put(p.getName(), R.REGEX_DOUBLE);
                }else{
                    if(param < method.getParameterCount()){
                        parameters.put(p.getName(), R.REGEX_WORD);
                    }else{
                        parameters.put(p.getName(), R.REGEX_ALL);
                    }
                }

                param++;
            }
        }
    }

    public Command getCommand(){
        return command;
    }

    public Method getMethod(){
        return method;
    }

    public String getParameterRegex(){
        return Joiner.on(" ").join(parameters.values());
    }

    public String getParamaterNames(){
        return Joiner.on(" ").join(parameters.keySet());
    }
}
