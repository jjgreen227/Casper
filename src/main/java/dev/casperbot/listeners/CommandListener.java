package dev.casperbot.listeners;

import dev.casperbot.*;
import dev.casperbot.database.cont.guild.*;
import dev.casperbot.database.cont.user.*;
import dev.casperbot.util.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.exceptions.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.requests.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        MessageChannel messageChannel = event.getMessageChannel();
        MessageHistory history = messageChannel.getHistory();

        switch (event.getName()) {
            case "guild" -> {
                // Fetch guild information from database and list them in an embed.
                Guild guild = event.getGuild();
                if (guild == null) return;
                String id = guild.getId();
                CasperGuild casperGuild = CasperGuildManager.getInstance().getGuild(id);
                if (casperGuild == null) return;
                casperGuild.info(event);
                event.reply("Guild info").setEphemeral(true).queue();
            }
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
                event.reply(history.size() + " messages deleted.").setEphemeral(true).queue();
            }
            case "info" -> {
                Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
                if (member == null) return;
                CasperUser user = new CasperUser(member.getId(), member.getEffectiveName());
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

    private void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(content))
                .queue(null, new ErrorHandler()
                        .handle(ErrorResponse.CANNOT_SEND_TO_USER, error -> System.out.println("Cannot send message to user")));
    }

    private void shutdown() {
        Main.api.shutdown();
    }
}
