package dev.casperbot.database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static dev.casperbot.util.CasperConstants.*;

@Getter
public class MySQLConnector {

    private final DatabaseToken info; // This is a class that contains the database token information;
    private Connection connection;

    public MySQLConnector(DatabaseToken info) {
        this.info = info;
    }

    public void connect()  {
        warning("Attempting to find database connection...");
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" + info.getDatabaseName(),
                    info.getUsername(), info.getPassword());
            fine("Successfully connected to database.");
        } catch (SQLException e) {
            severe("Unable to connect to database!");
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
