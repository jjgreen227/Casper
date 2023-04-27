package dev.casperbot.database;

import dev.casperbot.*;

import java.sql.*;

import static dev.casperbot.util.CasperConstants.*;

public class QueryHandler  {

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
        String insertQuery = "INSERT INTO guilds (guild_id, guild_name, guild_owner_id, guild_owner_name, guild_member_size) VALUES (?, ?, ?, ?, ?)";
        String selectQuery = "SELECT * FROM guilds";
        try {
            PreparedStatement statement;
            statement = Main.connector.getConnection().prepareStatement(selectQuery);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                info(String.format("Guild with ID %s already exists in database!", rs.getString("guild_id")));
                return;
            }
            statement = Main.connector.getConnection().prepareStatement(insertQuery);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, ownerId);
            statement.setString(4, ownerName);
            statement.setInt(5, memberSize);
            statement.executeUpdate();
            statement.close();
            info("Guild added to database!");
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
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("guild_id");
                String name = resultSet.getString("guild_name");
                String ownerId = resultSet.getString("guild_owner_id");
                String ownerName = resultSet.getString("guild_owner_name");
                int memberSize = resultSet.getInt("guild_member_size");
                fine("Guild ID: " + id + ", Guild Name: " + name + ", Guild Owner ID: " + ownerId + ", Guild Owner Name: " + ownerName + ", Guild Member Size: " + memberSize);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
