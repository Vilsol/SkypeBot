package io.mazenmc.skypebot.engine.bot;

import com.google.common.base.Joiner;
import io.mazenmc.skypebot.utils.Util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandData {

    private Command command;
    private Method method;
    private LinkedHashMap<String, ParameterData> parameters = new LinkedHashMap<>();

    public CommandData(Command c, Method method) {
        this.command = c;
        this.method = method;

        if (method.getParameterCount() > 1) {
            int param = 1;
            for (Parameter p : method.getParameters()) {
                if (param == 1) {
                    param++;
                    continue;
                }

                String regex = "";

                if (p.getType().equals(Integer.class) || p.getType().getName().equals("int")) {
                    regex = Util.REGEX_INT;
                } else if (p.getType().equals(Double.class) || p.getType().getName().equals("double")) {
                    regex = Util.REGEX_DOUBLE;
                } else if (p.getType().equals(String.class)) {
                    if (param < method.getParameterCount()) {
                        regex = Util.REGEX_WORD;
                    } else {
                        regex = Util.REGEX_ALL;
                    }
                }

                boolean optional = p.getAnnotation(Optional.class) != null;
                ParameterData data = new ParameterData(p.getName(), regex, optional);
                parameters.put(p.getName(), data);

                param++;
            }
        }
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public String getParamaterNames() {
        List<String> names = new LinkedList<>();

        parameters.values().stream().forEach(p -> {
            if (p.isOptional()) {
                names.add("[" + p.getName() + "]");
            } else {
                names.add("<" + p.getName() + ">");
            }
        });

        return Joiner.on(" ").join(names);
    }

    public String getParameterRegex(boolean includeOptional) {
        List<String> regex = new LinkedList<>();

        parameters.values().stream().forEach(p -> {

            if (p.isOptional()) {
                if (includeOptional) {
                    regex.add(p.getRegex());
                }
            } else {
                regex.add(p.getRegex());
            }

        });

        return Joiner.on(" ").join(regex);
    }
}
