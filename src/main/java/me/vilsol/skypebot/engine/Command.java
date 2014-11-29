package me.vilsol.skypebot.engine;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    public String name();
    public boolean exact() default true;
    public String[] alias() default {};
    public String[] allow() default {};
    public boolean command() default true;

}
