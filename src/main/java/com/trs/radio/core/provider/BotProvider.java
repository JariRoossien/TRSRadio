package com.trs.radio.core.provider;

import com.trs.radio.core.commands.CommandListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

@Configuration
public class BotProvider {

    @Getter
    private JDA jda;

    @Getter
    private CommandListener commandListener;

    public BotProvider(@Value("${discord.token}") String token) {
        try {
            jda = JDABuilder.create(token, GUILD_MESSAGES, GUILD_VOICE_STATES, GUILD_MEMBERS).build().awaitReady();
            this.commandListener = new CommandListener();
            jda.addEventListener(commandListener);
        } catch (LoginException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
