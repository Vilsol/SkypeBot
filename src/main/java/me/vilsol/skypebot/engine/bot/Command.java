package me.vilsol.skypebot.engine.bot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    public String name();
    public boolean exact() default true;
    public String[] alias() default {};
    public String[] disallow() default {};
    public boolean command() default true;
    public boolean admin() default false;

}
