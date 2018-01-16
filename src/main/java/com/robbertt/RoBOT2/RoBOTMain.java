package com.robbertt.RoBOT2;

import com.robbertt.RoBOT2.config.BotConfiguration;
import jdk.nashorn.internal.scripts.JD;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.aeonbits.owner.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Robert on 11/10/2017.
 */
public class RoBOTMain {

    public static BotConfiguration cfg = null;
    public static JDA jda = null;

    public static void main(String[] args) {
        cfg = ConfigFactory.create(BotConfiguration.class);
        Logger.getAnonymousLogger().log(Level.INFO, "Token: " + cfg.token());
        //create instance
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(cfg.token()).buildBlocking();
            jda.getPresence().setGame(Game.of(";cmds for commands.", "https://robbertt.com"));
            jda.addEventListener(new MessageHandler());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static JDA getJda() {
        return jda;
    }
}
