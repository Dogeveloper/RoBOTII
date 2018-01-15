package com.robbertt.RoBOT2;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Robert on 11/10/2017.
 */
public class MessageFormatUtil {
    public static boolean checkFormatting(String commandName, GuildRole role, MessageReceivedEvent data) {
        if(data.getAuthor().isBot()) {
            return false; //prevent looping responses glitch.
        }
        if(!checkPerms(data.getMember(), data.getGuild(), role) && (data.getChannelType().equals(ChannelType.TEXT) && data.getMessage().getRawContent().toLowerCase().startsWith(RoBOTMain.cfg.prefix() + commandName))) {
            data.getTextChannel().sendMessage(data.getAuthor().getAsMention() + " :no_entry: This command requires the permission role " + role.toString() + ".").queue();
            return false;
        }
        return data.getChannelType().equals(ChannelType.TEXT) && data.getMessage().getRawContent().toLowerCase().startsWith(RoBOTMain.cfg.prefix() + commandName);
    }

    private static boolean checkPerms(Member member, Guild g, GuildRole role) {
        try {
            switch (role) {
                case USER:
                    return true; //for now
                case MODERATOR:
                    return (g.getMembersWithRoles(g.getRolesByName(RoBOTMain.cfg.modRole(), true)).contains(member));
                case ADMIN:
                    return (g.getMembersWithRoles(g.getRolesByName(RoBOTMain.cfg.adminRole(), true)).contains(member));
                default:
                    return false; //this should not happen though.

            }
        }
        catch(Exception e) {
            //this occurs if the role does not exist. For security purposes, deny access.
            e.printStackTrace(); //TODO: remove this for production use.
            return false;
        }
    }

    public static Optional<ArrayList<String>> parseArguments(String commandText, String commandName, Member user, TextChannel channel, ArgumentValidator... argsToCheck) {
        ArrayList<String> text = new ArrayList<String>(Arrays.asList(commandText.split(" ")));
        ArrayList<ArgumentValidator> validation = new ArrayList<>(Arrays.asList(argsToCheck));
        ArrayList<ArgumentValidator> nonoptionals = new ArrayList<>(validation);
        nonoptionals.removeIf(i -> i.isOptional());
        text.remove(0); //remove command string
        Iterator<String> i1 = text.iterator();
        Iterator<ArgumentValidator> i2 = validation.iterator();
        if (text.size() < nonoptionals.size()) {
            channel.sendMessage(user.getAsMention() + " :warning: Incorrect amount of parameters! " + createCommandUsage(commandName, validation, user)).queue();
            return Optional.empty();
        }
        // optional parameters are still validated even though they are optional.
        while(i1.hasNext() && i2.hasNext()) {
            if(!i2.next().validate(i1.next())) {
                channel.sendMessage(user.getAsMention() + " :warning: Incorrect parameters! " + createCommandUsage(commandName, validation, user)).queue();
                return Optional.empty();
            }
        }
        return Optional.of(text);
    }

    public static String createCommandUsage(String commandName, ArrayList<ArgumentValidator> argsToCheck, Member user) {
        StringBuilder sb = new StringBuilder(); //create arguments
                sb.append(RoBOTMain.cfg.prefix())
                .append(commandName)
                .append(" ");
        for(ArgumentValidator v : argsToCheck) {
            sb.append(v.isOptional() ? "[" : "<").append(v.getCommandName()).append(v.isOptional() ? "] " : "> ");
        }
        return sb.toString();
    }

}
