package dev.casperbot.database.cont.guild;

import dev.casperbot.database.*;
import lombok.*;

import java.util.*;

@Getter
public class CasperGuildManager {
    private static CasperGuildManager instance;

    private final Map<String, CasperGuild> casperGuildMap;
    private CasperGuild guild;

    private CasperGuildManager() {
        this.casperGuildMap = new HashMap<>();
    }

    public void addGuild(String id, String name, String ownerId, String ownerName, int members) {
        if (this.casperGuildMap.containsKey(id)) return;
        this.guild = new CasperGuild(id, name, ownerId, ownerName, members);
        QueryHandler.addGuild(id, name, ownerId, ownerName, members);
        this.casperGuildMap.put(id, this.guild);
    }

    public CasperGuild getGuild(String id) {
        QueryHandler.getGuildInfo(id);
        return this.casperGuildMap.get(id);
    }

    public static CasperGuildManager getInstance() {
        if (instance == null) instance = new CasperGuildManager();
        return instance;
    }
}
