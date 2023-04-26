package dev.casperbot.database;

import dev.casperbot.*;

import java.sql.*;

public class MySQLFactory {

    public static void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS discord_users (discord_id VARCHAR(18), discord_name VARCHAR(255))";
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
}
