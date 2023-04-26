package dev.casperbot.database.cont.user;

import dev.casperbot.database.*;
import dev.casperbot.util.*;

import java.util.*;

public class DiscordUserManager {
    private static DiscordUserManager instance;
    private final Map<String, String> discordUserMap;
    private DiscordUser user;

    private DiscordUserManager(DiscordUser user) {
        this.user= user;
        this.discordUserMap = new HashMap<>();
    }

    public void addDiscordUser(String id, String name) {
        if (this.discordUserMap.containsKey(id)) return; // So we don't have to continuously check if the user is in the database.
        this.discordUserMap.put(id, name);
        MySQLFactory.addDiscordUser(id, name);
        CasperConstants.fine("Added user " + name + " to the database.");
    }

    public void removeDiscordUserCache(DiscordUser user) {
        this.discordUserMap.remove(user);
    }

    public static DiscordUserManager getInstance(DiscordUser user) {
        if (instance == null) instance = new DiscordUserManager(user);
        return instance;
    }

}
