package dev.casperbot.handlers;

import dev.casperbot.util.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.channel.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.message.*;

import java.awt.*;
import java.time.format.*;

/*
*  This class will definitely be changed in the future.
*  But consider this as a placeholder for now.
*  This class will be used to handle all audit log events.
*/
public class AuditLogHandlers extends Handler {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        final User user = event.getAuthor();
        final Message message = event.getMessage();
        final Member member = event.getMember();
        final Guild guild = event.getGuild();
        final TextChannel channel = guild.getTextChannelById("1086685016915968111");

        if (user.isBot()) return;
        if (member == null) return;
        if (channel == null) return;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Chat History");
        builder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        builder.setImage("https://i.imgur.com/pP4MHTW.png");
        builder.setColor(Color.GREEN);
        builder.setThumbnail(user.getEffectiveAvatarUrl());
        builder.addField("Message", message.getContentDisplay(), false);
        builder.setFooter(user.getAsTag());
        channel.sendTyping().queue(voided -> channel.sendMessageEmbeds(builder.build()).queue());
//        createEmbed(channel);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final Member member = event.getMember();
        final Guild guild = event.getGuild();
        final Role role = guild.getRoleById("1059342039042502736"); // Insert config option here.
        final TextChannel channel = guild.getTextChannelById("1093722147681218661"); // Insert config option here.
        final TextChannel auditChannel = guild.getTextChannelById("1086685016915968111");
        if (channel == null) return;
        if (auditChannel == null) return;
        if (role == null) return;
        guild.addRoleToMember(member, role).queue();
        channel.sendTyping().queue(voided -> channel.sendMessage(member.getAsMention() + " has joined.").queue());
        EmbedUtil.createMemberJoinEmbed(
                auditChannel,
                member.getUser().getAsTag(),
                member.getId(),
                member.getTimeJoined().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
