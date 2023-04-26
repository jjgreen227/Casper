package dev.casperbot.util;

import lombok.Getter;

import java.io.*;
import java.util.Properties;

@Getter
public class FileSetup {
    private final String fileName;

    private File file;
    private Properties properties;

    public String host;
    public int port;
    public String database;
    public String username;
    public String password;
    public String token;

    public FileSetup(String fileName) {
        this.fileName = fileName;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() {
        file = new File(fileName);
        if (!file.exists()) {
            CasperConstants.warning("File " + fileName + " does not exist. Creating file...");
            try {
                file.createNewFile();
                CasperConstants.fine("Created file");
            } catch (Exception e) {
                e.printStackTrace();
                CasperConstants.severe("Failed to create file");
            }
        }
        CasperConstants.fine("File " + fileName + " exists");
    }

    public void setupReader() {
        try {
            FileReader reader = new FileReader(this.file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            this.properties = new Properties();
            this.properties.load(bufferedReader);
            this.host = this.properties.getProperty("host");
            this.port = Integer.parseInt(this.properties.getProperty("port"));
            this.database = this.properties.getProperty("database");
            this.username = this.properties.getProperty("username");
            this.password = this.properties.getProperty("password");
            this.token = this.properties.getProperty("token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
