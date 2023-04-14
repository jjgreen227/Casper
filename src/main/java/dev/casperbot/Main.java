package dev.casperbot;

import dev.casperbot.database.*;
import dev.casperbot.handlers.*;
import dev.casperbot.listeners.*;
import dev.casperbot.util.*;
import dev.casperbot.util.exc.*;
import dev.casperbot.util.file.*;
import lombok.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.requests.*;
import net.dv8tion.jda.api.utils.*;

import java.sql.*;
import java.util.*;

import static dev.casperbot.util.CasperConstants.*;
import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;

@Getter
public class Main {

    private static JDA api;

    private static final GatewayIntent[] allIntents = new GatewayIntent[]{
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGE_TYPING,
            GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.GUILD_VOICE_STATES
    };

    private static void setup() {
        CasperFile file = new CasperFile("config.yml");
        MySQLConnector connector = new MySQLConnector(new MySQLConnector.DatabaseToken(
                "localhost", 3306, "bookstore", "root", "myboot23"));
        info("Attempting to find database connection...");
        try {
            connector.connect();
            fine("Successfully connected to database.");
        } catch (SQLException e) {
            severe("Unable to connect to database!");
            throw new RuntimeException(e);
        }
        JDABuilder jdaBuilder = JDABuilder.createDefault(CasperConstants.TOKEN, Arrays.asList(allIntents));
        jdaBuilder.setActivity(Activity.streaming("IntelliJ Code", "https://www.twitch.tv/jayboy329"));
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.setBulkDeleteSplittingEnabled(false);
        registerListeners(jdaBuilder);
        api = jdaBuilder.build();
        fine("Successfully built JDA instance.");
        try {
            api.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        addRoleToEveryone();
        registerCommands();
    }

    private static void addRoleToEveryone() {
        info("Attempting to add a role to everyone...");
        try {
            Role role = api.getRoleById("1059342039042502736"); // Insert config option here.
            if (role == null) throw new RoleNotFoundException(role.getName());
            Guild guild = api.getGuildById(CasperConstants.GUILD_ID); // Insert config option here.
            if (guild == null) throw new GuildNotFoundException("Unable to find guild with ID: " + CasperConstants.GUILD_ID);
            List<Member> members = guild.getMembers();
            members.forEach(member -> {
                if (member.getUser().isBot()) return;
                if (member.hasPermission(Permission.ADMINISTRATOR)) return; // Subject to change in the future.
                if (!member.getRoles().contains(role)) {
                    info("Adding role to: " + member.getUser().getName());
                    guild.addRoleToMember(member, role).queue();
                }
            });
            fine("Successfully added roles to everyone.");
        } catch (Exception e) {
            severe("Unable to add role to everyone!");
            throw new RuntimeException(e);
        }
    }

    private static void registerListeners(JDABuilder builder) {
        info("Registering listeners...");
        try {
            builder.addEventListeners(
                    new CommandListener(),
                    new AuditLogHandlers(),
                    new VoiceChannelListener(),
                    new HistoryListener(),
                    new RPS());
//                    new GuildUpdateListener(),
        } catch (Exception e) {
            severe("Unable to register listeners!");
            e.printStackTrace();
            api.shutdown();
        }
        fine("Successfully registered all listeners.");
    }

    private static void registerCommands() {
        info("Registering Commands...");
        try {
            final SlashCommandData ping = slash("ping", "Ping");
            final SlashCommandData shutdown = slash("shutdown", "Shuts down the bot (hopefully in a fast manner)")
                    .setGuildOnly(true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            final SlashCommandData clearchat = slash("clearchat", "Purges a certain amount of chat lines.")
                    .addOption(OptionType.INTEGER, "lines", "Clears how many lines we can get.", false)
                    .setGuildOnly(true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            final SlashCommandData info = slash("info", "Printing User Information")
                    .addOption(OptionType.USER, "user", "User information", true)
                    .setGuildOnly(true);
            final SlashCommandData test = slash("test", "Testing out embed for EmbedBuilder")
                    .setGuildOnly(true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            final SlashCommandData rps = slash("rps", "Rock Paper Scissors...Shoot!");
            api.updateCommands().addCommands(
                    ping,
                    shutdown,
                    clearchat,
                    info,
                    rps,
                    test
            ).queue();
        } catch (Exception e) {
            e.printStackTrace();
            severe("Unable to register commands!");
            api.shutdown();
        }
        fine("Successfully registered all commands.");
    }

    public static JDA getApi() {
        return api;
    }

    public static void main(String[] args) {
        setup();
    }
}