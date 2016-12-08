package me.vilsol.skypebot.modules;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import me.vilsol.skypebot.engine.bot.Command;
import me.vilsol.skypebot.engine.bot.Module;
import me.vilsol.skypebot.utils.R;

import java.io.IOException;

public class Compiler implements Module {

    @Command(name = "run")
    public void runCode(ChatMessage message) throws SkypeException {
        final String code = message.getContent();
        Thread compilerThread = new Thread(() -> {
            try {
                CompilerConnection compilerConnection = new CompilerConnection();
                compilerConnection.setCode(code);
                compilerConnection.send();
                R.s(compilerConnection.getReport());
                R.s(compilerConnection.getResult());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        compilerThread.start();
    }
}
