package dev.casperbot.listeners;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.*;

import java.awt.*;

public class MessageReceiveListener extends ListenerAdapter {

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
    }
}
