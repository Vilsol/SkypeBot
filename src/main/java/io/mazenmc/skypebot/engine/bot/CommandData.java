package io.mazenmc.skypebot.engine.bot;

import com.google.common.base.Joiner;
import io.mazenmc.skypebot.engine.bot.generic.StringResponse;
import io.mazenmc.skypebot.utils.Resource;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandData {

    private CommandInternal command;
    private Method method;
    private StringResponse response;
    private LinkedHashMap<String, ParameterData> parameters = new LinkedHashMap<>();

    public CommandData(CommandInternal c, Method method) {
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
                    regex = Resource.REGEX_INT;
                } else if (p.getType().equals(Double.class) || p.getType().getName().equals("double")) {
                    regex = Resource.REGEX_DOUBLE;
                } else if (p.getType().equals(String.class)) {
                    if (param < method.getParameterCount()) {
                        regex = Resource.REGEX_WORD;
                    } else {
                        regex = Resource.REGEX_ALL;
                    }
                }

                boolean optional = p.getAnnotation(Optional.class) != null;
                ParameterData data = new ParameterData(p.getName(), regex, optional);
                parameters.put(p.getName(), data);

                param++;
            }
        }
    }

    public CommandData(CommandInternal c, StringResponse response) {
        this.command = c;
        this.response = response;
    }

    public CommandInternal getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public StringResponse getResponse() {
        return response;
    }

    public String getParameterNames() {
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
