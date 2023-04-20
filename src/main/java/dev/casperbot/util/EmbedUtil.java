package dev.casperbot.util;

import lombok.experimental.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.interaction.command.*;

import java.awt.*;

@UtilityClass
public class EmbedUtil {

    public void createMemberJoinEmbed(TextChannel channel, String value, String value2, String value3) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("User Joined");
        embedBuilder.setColor(Color.RED);
        embedBuilder.addField("Discord Tag", value, true); // field = "Discord Tag", value = member.getAsTag()
        embedBuilder.addField("User ID", value2, true); // field = "User ID", value = member.getId()
        embedBuilder.addField("Joined Date", value3, true); // field = "Joined Server", value = member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        embedBuilder.setAuthor("Audit Log", "https://i.imgur.com/pP4MHTW.png", "https://i.imgur.com/pP4MHTW.png");
        channel.sendTyping().queue(voided -> channel.sendMessageEmbeds(embedBuilder.build()).queue());
    }

    public void testEmbed(TextChannel channel, boolean isCommand) {
        SlashCommandInteractionEvent event = null;
        if (isCommand) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Test Embed");
            builder.setDescription("This is a test embed.");
            builder.setColor(Color.DARK_GRAY);
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Test Embed");
            builder.setDescription("This is a test embed.");
            builder.setColor(Color.DARK_GRAY);
            channel.sendTyping().queue(voided -> channel.sendMessageEmbeds(builder.build()).queue());
        }
    }
}
