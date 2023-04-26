package dev.casperbot;

import dev.casperbot.automod.*;
import dev.casperbot.database.*;
import dev.casperbot.handlers.*;
import dev.casperbot.listeners.*;
import dev.casperbot.util.*;
import dev.casperbot.util.exc.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.requests.*;
import net.dv8tion.jda.api.utils.*;

import java.io.*;
import java.sql.*;
import java.util.*;

import static dev.casperbot.util.CasperConstants.*;
import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;

public class Main {

    public static JDA api;
    public static MySQLConnector connector;

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

    // TODO: Make this cleaner please. It hurts everyone's eyes.
    /**
     * This is the init method. This method is called when the bot is started.
     *
     * It will find a properties file, if not created then it will create and load properties.
     * Once properties are loaded, it will connect to the database.
     * If the database connection fails, it will throw an exception.
     *
     * Once database has been connected then it will start to execute
     * any methods that need to be executed.
     *
     * JDA methods start to execute here after database connection and method implementation.
     *
     *
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    private static void init() throws IOException, SQLException, InterruptedException {
        /*
            * This is the setup method. This method is called when the bot is started.
            * This method will load the config.properties file, and then connect to the database.
            * If the file does not exist, it will create the file.
            * If the database connection fails, it will throw an exception.
    /*
     * This is the setup method. This method is called when the bot is started.
     * This method will load the config.properties file, and then connect to the database.
     * If the file does not exist, it will create the file.
     * If the database connection fails, it will throw an exception.

     * This also looks very messy, but it's just a bunch of try-catch blocks.
     * There definitely will be a better way to do this in the future.
     */
    private static void init() throws IOException, SQLException, InterruptedException {
        FileSetup fileSetup = new FileSetup("config.properties");
        fileSetup.setup();
        fileSetup.setupReader();
        String host = fileSetup.getHost();
        int port = fileSetup.getPort();
        String database = fileSetup.getDatabase();
        String username = fileSetup.getUsername();
        String password = fileSetup.getPassword();
        String token = fileSetup.getToken();
        connector = new MySQLConnector(new MySQLConnector.DatabaseToken(
                host,
                port,
                database,
                username,
                password));
        connector.connect();
        MySQLFactory.createTable();
        warning("Attempting to build JDA instance...");
        JDABuilder jdaBuilder = JDABuilder.createDefault(token, Arrays.asList(allIntents));
        jdaBuilder.setActivity(Activity.streaming("IntelliJ Code", "https://www.twitch.tv/jayboy329"));
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.setBulkDeleteSplittingEnabled(false);
        registerListeners(jdaBuilder);
        api = jdaBuilder.build();
        api.awaitReady();
        fine("Successfully built JDA instance.");
        registerCommands();
        createLogs();
    }

    private static void registerListeners(JDABuilder builder) {
        warning("Registering listeners...");
        try {
            builder.addEventListeners(
                    new CommandListener(),
                    new DiscordUserFactory(),
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
        warning("Registering Commands...");
        try {
            var ping = slash("ping", "Ping");
            var shutdown = slash("shutdown", "Shuts down the bot (hopefully in a fast manner)")
                    .setGuildOnly(true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            var clearchat = slash("clearchat", "Purges a certain amount of chat lines.")
                    .addOption(OptionType.INTEGER, "lines", "Clears how many lines we can get.", false)
                    .setGuildOnly(true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            var info = slash("info", "Printing User Information")
                    .addOption(OptionType.USER, "user", "User information", true)
                    .setGuildOnly(true);
            var test = slash("test", "Testing out embed for EmbedBuilder")
                    .setGuildOnly(true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            var rps = slash("rps", "Rock Paper Scissors...Shoot!");
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

    private static void createLogs() {
        Guild guild = api.getGuildById(CasperConstants.GUILD_ID);
        try {
            if (guild == null) throw new GuildNotFoundException("Unable to find guild with ID: " + CasperConstants.GUILD_ID);
            if (guild.getCategoriesByName("test", true).isEmpty()) {
                severe("Unable to find support category. Creating category...");
                guild.createCategory("test").queue();
                fine("Category has been created.");
                Thread.sleep(1000);
                info("Creating channels within category...");
                guild.getCategoriesByName("test", true).forEach(category -> {
                    category.createTextChannel("audit-log").setTopic("Logging Moderation Actions.").queue();
                    category.createTextChannel("changelog").setTopic("Logging GitHub Actions.").queue();
                });
                fine("Channels created.");
            } else {
                fine("Category exists.");
            }
        } catch (GuildNotFoundException | InterruptedException e) {
            severe("Unable to create channels.");
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        init();
    }
}