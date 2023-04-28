package dev.casperbot;

import dev.casperbot.database.MySQLConnector;
import dev.casperbot.database.QueryHandler;
import dev.casperbot.database.cont.guild.*;
import dev.casperbot.handlers.AuditLogHandlers;
import dev.casperbot.listeners.CommandListener;
import dev.casperbot.listeners.HistoryListener;
import dev.casperbot.listeners.VoiceChannelListener;
import dev.casperbot.util.CasperConstants;
import dev.casperbot.util.FileSetup;
import dev.casperbot.util.exc.GuildNotFoundException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.sql.SQLException;
import java.util.Arrays;

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

    /**
     * This is the init method. This method is called when the bot is started.
     * <p>
     * It will find a properties file, if not created then it will create and load properties.
     * Once properties are loaded, it will connect to the database.
     * If the database connection fails, it will throw an exception.
     * <p>
     * Once database has been connected then it will start to execute
     * any methods that need to be executed.
     * <p>
     * JDA methods start to execute here after database connection and method implementation.
     *
     * @throws SQLException
     * @throws InterruptedException
     */
    private static void init() throws SQLException, InterruptedException {
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
        QueryHandler.createGuildTable();
        QueryHandler.createUserTable();
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
    }

    private static void registerListeners(JDABuilder builder) {
        warning("Registering listeners...");
        try {
            builder.addEventListeners(
                    new CasperGuildListener(),
                    new CommandListener(),
                    new AuditLogHandlers(),
                    new VoiceChannelListener(),
                    new HistoryListener(),
                    new RPS());
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
            var guild = slash("guild", "Guild Information")
                    .setGuildOnly(true);
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
                    guild,
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

    public static void main(String[] args) throws SQLException, InterruptedException {
        init();
    }
}