package dev.casperbot.database;

import lombok.*;

import java.sql.*;

@Getter
public class MySQLConnector {

    private final DatabaseToken info; // This is a class that contains the database token information;
    private Connection connection;

    public MySQLConnector(DatabaseToken info) {
        this.info = info;
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" + info.getDatabaseName(),
                    info.getUsername(), info.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    public static class DatabaseToken {
        String host;
        int port;
        String databaseName;
        String username;
        String password;

        public DatabaseToken(String host, int port, String databaseName, String username, String password) {
            this.host = host;
            this.port = port;
            this.databaseName = databaseName;
            this.username = username;
            this.password = password;
        }
    }
}
