package dev.casperbot.listeners;

import dev.casperbot.*;
import dev.casperbot.database.cont.user.*;
import dev.casperbot.util.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.exceptions.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.requests.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.util.*;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        MessageChannel messageChannel = event.getMessageChannel();
        MessageHistory history = messageChannel.getHistory();

        switch (event.getName()) {
            case "ping" -> {
                long ping = System.currentTimeMillis() - System.currentTimeMillis();
                event.reply("Ping")
                        .setEphemeral(true)
                        .flatMap(v -> event.getHook().editOriginal("Pinging: " + ping + " ms"))
                        .queue();
            }
            case "clearchat" -> {
                int option = event.getOption("lines", 1, OptionMapping::getAsInt);
                history.retrievePast(option).queue((messages -> event.getChannel().purgeMessages(messages)));
                reply(event, "Successfully cleared chat.", true);
            }
            case "info" -> {
                Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
                if (member == null) return;
                DiscordUser user = new DiscordUser(member.getId(), member.getEffectiveName());
                user.info(event, member);
            }
            case "shutdown" -> {
                sendPrivateMessage(event.getUser(), "Shutting down...");
                event.reply("Goodbye...").setEphemeral(true).queue();
                shutdown();
            }
            case "test" -> EmbedUtil.testEmbed(event.getGuildChannel().asTextChannel(), true);
            case "rps" -> {
                RPS rps = new RPS();
                rps.start(event);
            }
            default -> throw new IllegalStateException("Unexpected value: " + event.getName());
        }
    }

    private void testEmbed(SlashCommandInteractionEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Test Embed");
        builder.setAuthor("Casper", "https://i.imgur.com/pP4MHTW.png", "https://i.imgur.com/pP4MHTW.png");
        builder.setDescription("This is a test embed.");
        builder.addField("Field 1", "This is field 1.", true);
        builder.addField("Field 2", "This is field 2.", true);
        builder.addField("Field 3", "This is field 3.", true);
        builder.addField("Field 4", "This is field 4.", true);
        builder.addField("Field 5", "This is field 5.", true);
        builder.setImage("https://i.imgur.com/pP4MHTW.png");
        builder.setColor(Color.GREEN);
        builder.setThumbnail("https://i.imgur.com/pP4MHTW.png");
        builder.setFooter("Footer");
        builder.setTimestamp(new Date().toInstant());
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    private void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(content))
                .queue(null, new ErrorHandler()
                        .handle(ErrorResponse.CANNOT_SEND_TO_USER, error -> System.out.println("Cannot send message to user")));
    }

    private void shutdown() {
        Main.api.shutdown();
    }

    private void reply(SlashCommandInteractionEvent event, String content, boolean ephemeral) {
        event.reply(content).setEphemeral(ephemeral).queue();
    }
}
