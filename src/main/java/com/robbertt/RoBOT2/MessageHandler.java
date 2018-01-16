package com.robbertt.RoBOT2;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.robbertt.RoBOT2.config.BotConfiguration;
import com.wolfram.alpha.*;
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
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
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
        if(e.getMessage().getRawContent().toLowerCase().equals("quotemeup") && !e.getAuthor().isBot()) {
            try {
                HttpResponse<JsonNode> response = Unirest.post("https://andruxnet-random-famous-quotes.p.mashape.com/?cat=famous&count=1")
                        .header("X-Mashape-Key", RoBOTMain.cfg.apiKey())
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .asJson();
                System.out.println(response.getBody().toString());
                MessageEmbed message = new EmbedBuilder().setTitle("Quote").addField("", response.getBody().getObject().getString("quote"), false).addField("Author", response.getBody().getObject().getString("author"), true).addField("Category", response.getBody().getObject().getString("category"), true).setColor(Color.BLUE).build();
                e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor().getAsMention()).setEmbed(message).build()).queue();
            }
            catch(Exception ex) {
                ex.printStackTrace();
                e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " :interrobang: An error occurred while attempting to perform this command. Contact Monsieur_Robert for help.").queue();
            }
        }
        if(e.getMessage().getRawContent().toLowerCase().startsWith("solve ")) {
            //query wolfram alpha for information
            System.out.println("Message got there.");
            final String wolframappid = RoBOTMain.cfg.wolframappid();
            final String message = e.getMessage().getRawContent().replaceAll("solve ", "");

            WAEngine engine = new WAEngine();
            engine.setAppID(wolframappid);
            engine.addFormat("plaintext");
            WAQuery q = engine.createQuery();
            q.setInput(message);
            try {
                WAQueryResult result = engine.performQuery(q);
                //begin construction of message.
                EmbedBuilder b = new EmbedBuilder().setColor(Color.RED).setTitle("Query").setDescription(message).setAuthor("WolframAlpha", "https://www.wolframalpha.com/", "http://company.wolfram.com/data/press-center/uploads/2016/12/wa-logo-stacked-large.jpg");
                for(WAPod pod : result.getPods()) {
                    b.addField(pod.getTitle(), "=---------=", false);
                    for(WASubpod subpod : pod.getSubpods()) {
                        StringBuilder sb = new StringBuilder();
                        for(Object element : subpod.getContents()) {
                            if (((WAPlainText) element).getText().equals("")) {
                                sb.append("**Not Yet Supported**");
                            }
                            else {
                                sb.append(((WAPlainText) element).getText()).append(" ");
                            }
                        }
                        b.addField(subpod.getTitle(), sb.toString(), true);
                    }
                }
                e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor().getAsMention()).setEmbed(b.build()).build()).queue();
            }
            catch(Exception ex) {
                e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + "An error occurred while attempting to perform this query. This may be because the bot was rate-limited by Wolfram Alpha.").queue();

            }

        }
        if(e.getMessage().getRawContent().toLowerCase().startsWith("rolecolor")) {
            String[] split = e.getMessage().getRawContent().split(" ");
            if(split.length < 2) {
                
            }
        }

    }
}
