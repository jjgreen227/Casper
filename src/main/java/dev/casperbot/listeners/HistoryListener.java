package dev.casperbot.listeners;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.*;

import java.awt.*;

public class HistoryListener extends ListenerAdapter {

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        Guild guild = event.getGuild();
        final TextChannel channel = guild.getTextChannelById("1086685016915968111");
        if (channel == null) return;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Chat History");
        builder.setColor(Color.RED);

//        builder.setThumbnail(user.getEffectiveAvatarUrl());
//        builder.addField("Message", message.getContentDisplay(), false);
//        builder.setFooter(user.getAsTag());
//        channel.sendTyping().queue(voided -> channel.sendMessageEmbeds(builder.build()).queue());
    }
}
