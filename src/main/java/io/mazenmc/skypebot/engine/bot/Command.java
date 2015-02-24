package io.mazenmc.skypebot.engine.bot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    public boolean admin() default false;

    public String[] alias() default {};

    public boolean command() default true;

    public boolean exact() default true;

    public String name();

    public int cooldown() default 0;
}
