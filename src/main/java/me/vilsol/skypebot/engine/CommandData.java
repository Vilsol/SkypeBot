package me.vilsol.skypebot.engine;

import com.google.common.base.Joiner;
import me.vilsol.skypebot.R;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;

public class CommandData {

    private Command command;
    private Method method;
    private LinkedHashMap<String, String> parameters = new LinkedHashMap<>();

    public CommandData(Command c, Method method){
        this.command = c;
        this.method = method;

        if(method.getParameterCount() > 1){
            int param = 1;
            for(Parameter p : method.getParameters()){
                if(param == 1){
                    param++;
                    continue;
                }

                if(p.getType().equals(Integer.class) || p.getType().getName().equals("int")){
                    parameters.put(p.getName(), R.REGEX_INT);
                }else if(p.getType().equals(Double.class) || p.getType().getName().equals("double")){
                    parameters.put(p.getName(), R.REGEX_DOUBLE);
                }else if(p.getType().equals(String.class)){
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
