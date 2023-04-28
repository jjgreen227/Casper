package dev.casperbot.database;

import dev.casperbot.*;

import java.sql.*;

import static dev.casperbot.util.CasperConstants.*;

public class QueryHandler {

    public static void createUserTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS discord_users (discord_id VARCHAR(18), discord_name VARCHAR(255))";
        PreparedStatement statement = Main.connector.getConnection().prepareStatement(query);
        statement.executeUpdate();
    }

    public static void createGuildTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS guilds (guild_id VARCHAR(19), guild_name VARCHAR(255), guild_owner_id VARCHAR(18), guild_owner_name VARCHAR(255), guild_member_size INT)";
        PreparedStatement statement = Main.connector.getConnection().prepareStatement(query);
        statement.executeUpdate();
    }

    public static void addDiscordUser(String id, String name) { // this is ugly, but it works...
        String query = "INSERT INTO discord_users (discord_id, discord_name) VALUES (?, ?)";
        try {
            PreparedStatement statement = Main.connector.getConnection().prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addGuild(String id, String name, String ownerId, String ownerName, int memberSize) {
        warning("Adding guild to database...");
        String insertQuery = "INSERT INTO bookstore.guilds (guild_id, guild_name, guild_owner_id, guild_owner_name, guild_member_size) VALUES (?, ?, ?, ?, ?)";
        String selectQuery = "SELECT * FROM bookstore.guilds WHERE guild_id = ?";
        try {
            PreparedStatement statement;
            statement = Main.connector.getConnection().prepareStatement(selectQuery);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                info(String.format("Guild with ID %s already exists in database!", rs.getString("guild_id")));
            } else {
                info(String.format("Guild with ID %s does not exist in database!", id));
                statement = Main.connector.getConnection().prepareStatement(insertQuery);
                statement.setString(1, id);
                statement.setString(2, name);
                statement.setString(3, ownerId);
                statement.setString(4, ownerName);
                statement.setInt(5, memberSize);
                statement.executeUpdate();
                info("Guild added to database!");
            }
        } catch (Exception e) {
            severe("Failed to add guild to database!");
            throw new RuntimeException(e);
        }
    }

    public static void getGuildInfo(String guildId) {
        String query = "SELECT * FROM bookstore.guilds WHERE guild_id = ?";
        try {
            PreparedStatement statement = Main.connector.getConnection().prepareStatement(query);
            statement.setString(1, guildId);
            statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
