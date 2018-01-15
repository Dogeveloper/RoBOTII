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
        if(MessageFormatUtil.checkFormatting("ping", GuildRole.USER, e)) {
            e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " Pong!").queue();
        }
        if(MessageFormatUtil.checkFormatting("echotest", GuildRole.ADMIN, e)) {
            Optional<ArrayList<String>> l = MessageFormatUtil.parseArguments(e.getMessage().getRawContent(), "echotest", e.getMember(), e.getTextChannel(), new ArgumentValidator("message", false, argument -> true));
            if (l.isPresent()) {
                System.out.println("sending msg");
                e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " " + l.get().get(0)).queue();
            }
        }

        if(MessageFormatUtil.checkFormatting("kitten", GuildRole.USER, e)) {
            e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " http://www.randomkittengenerator.com/cats/rotator.php").queue();
        }
        if(MessageFormatUtil.checkFormatting("cmds", GuildRole.USER, e)) {
            e.getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessage("**RoBOT II Commands Listing: **").queue(); //TODO: find a better way to do this.
                channel.sendMessage("*Kitten*: Displays a picture of a kitten.").queue();
                channel.sendMessage("*Ping*: Pong!").queue();
                channel.sendMessage("*Coinflip*: Flips a coin.").queue();
                channel.sendMessage("*Echotest*: Echos a message.").queue();
            });
            e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + ", the commands have been PMed to you.").queue();
        }
        if(MessageFormatUtil.checkFormatting("coinflip", GuildRole.USER, e)) {
            SecureRandom numgen = new SecureRandom();
            e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + (numgen.nextBoolean() ? " Heads!" : " Tails!")).queue();
        }
        if(MessageFormatUtil.checkFormatting("rolldice", GuildRole.USER, e)) {
            Optional<ArrayList<String>> args = MessageFormatUtil.parseArguments(e.getMessage().getRawContent(), "rolldice", e.getMember(), e.getTextChannel(), new ArgumentValidator("sides", false, argument -> {
                int i = 0;
                try {
                    i = Integer.parseInt(argument);
                }
                finally {
                    if(i > 0) { //checks if the integer is present and is an actual number.
                        return true; //valid
                    }
                    else {
                        return false; //invalid
                    }
                }
            }));
            if(args.isPresent()) {
                ArrayList<String> arguments = args.get();
                SecureRandom r = new SecureRandom();
                int num = r.nextInt(Integer.parseInt(arguments.get(0)) - 1); //user chosen limit
                e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " You rolled a **" + num + "** out of a **" + arguments.get(0) + "** sided die.").queue();

            }
        }
        if(e.getMessage().getMentionedUsers().contains(RoBOTMain.getJda().getSelfUser())) {
            e.getTextChannel().getHistory().retrievePast(100).queue(m -> {
                MessageEmbed message = new EmbedBuilder().setTitle("Your message is ready.").setColor(Color.MAGENTA).addField("Message:", m.get(new SecureRandom().nextInt(99)).getRawContent(), false).build();
                e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor().getAsMention()).setEmbed(message).build()).queue();
            });
        }

    }
}
