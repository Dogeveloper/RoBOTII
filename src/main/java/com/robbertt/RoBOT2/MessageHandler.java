package com.robbertt.RoBOT2;

import jdk.nashorn.internal.parser.JSONParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Robert on 11/10/2017.
 */
public class MessageHandler extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getMessage().getMentionedUsers().contains(RoBOTMain.getJda().getSelfUser())) {
            e.getTextChannel().getHistory().retrievePast(100).queue(m -> {
                MessageEmbed message = new EmbedBuilder().setTitle("Your message is ready.").setColor(Color.MAGENTA).addField("Message:", m.get(new SecureRandom().nextInt(99)).getRawContent(), false).build();
                e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor().getAsMention()).setEmbed(message).build()).queue();
            });
        }

    }
}
