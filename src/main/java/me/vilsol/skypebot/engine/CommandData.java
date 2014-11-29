package me.vilsol.skypebot.engine;

import java.lang.reflect.Method;

public class CommandData {

    private Command command;
    private Method method;

    public CommandData(Command c, Method method){
        this.command = c;
        this.method = method;
    }

    public Command getCommand(){
        return command;
    }

    public Method getMethod(){
        return method;
    }
}
