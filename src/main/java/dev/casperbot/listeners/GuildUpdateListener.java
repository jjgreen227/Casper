package dev.casperbot.listeners;

import dev.casperbot.automod.*;
import dev.casperbot.util.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.*;

public class GuildUpdateListener extends ListenerAdapter {

    // Everytime a member joins the guild.
    // Add them to the cache and add them
    // to the database.

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final Member member = event.getMember();
        final Guild guild = event.getGuild();
        final Role role = guild.getRoleById("1059342039042502736"); // Insert config option here.
        final TextChannel channel = guild.getTextChannelById("1093722147681218661"); // Insert config option here.
        if (channel == null) return;
        if (role == null) return;
        guild.addRoleToMember(member, role).queue();
        channel.sendTyping().queue(voided -> channel.sendMessage(member.getAsMention() + " has joined.").queue());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        final Member member = event.getMember();
        DiscordUser dUser = new DiscordUser(member.getId());
        dUser.setName(member.getEffectiveName());
        CasperConstants.info("User " + dUser.getName() + " has left the server.");
        // Insert leave cache here

        final Guild guild = event.getGuild();
        final TextChannel channel = guild.getTextChannelById("1046314739682250814");
        if (channel == null) return;
        channel.sendTyping().queue(voided -> channel.sendMessage(member.getAsMention() + " has left.").queue());
    }
}

