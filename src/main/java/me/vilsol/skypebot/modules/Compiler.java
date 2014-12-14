package me.vilsol.skypebot.modules;

import me.vilsol.skypebot.engine.bot.Command;
import me.vilsol.skypebot.engine.bot.Module;
import me.vilsol.skypebot.utils.R;

import java.io.IOException;

public class Compiler implements Module {

    @Command(name = "run")
    public void runCode(final String code) {
        Thread compilerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CompilerConnection compilerConnection = new CompilerConnection();
                    compilerConnection.setCode(code);
                    compilerConnection.send();
                    R.s(compilerConnection.getReport());
                    R.s(compilerConnection.getResult());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        compilerThread.start();
    }
}
