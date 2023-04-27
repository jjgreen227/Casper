package dev.casperbot.database.cont.user;

import dev.casperbot.database.*;
import dev.casperbot.util.*;

import java.util.*;

public class CasperUserManager {
    private static CasperUserManager instance;
    private final Map<String, CasperUser> casperUserMap;
    private CasperUser user;

    private CasperUserManager(CasperUser user) {
        this.user= user;
        this.casperUserMap = new HashMap<>();
    }

    public void addDiscordUser(String id, String name) {
        if (this.casperUserMap.containsKey(id)) return;
        CasperUser user = new CasperUser(id, name);
        QueryHandler.addDiscordUser(id, name);
        this.casperUserMap.put(id, user);
        CasperConstants.fine("Added user " + name + " to the database.");
    }

    public void removeDiscordUserCache(CasperUser user) {
        if (!this.casperUserMap.containsKey(user.getId())) return;
        this.casperUserMap.remove(user);
    }

    public static CasperUserManager getInstance(CasperUser user) {
        if (instance == null) instance = new CasperUserManager(user);
        return instance;
    }

}
