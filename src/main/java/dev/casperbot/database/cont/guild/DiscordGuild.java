package dev.casperbot.database.cont.guild;

import lombok.*;

import java.lang.reflect.*;
import java.util.*;

@Getter
@Setter
public class DiscordGuild {

    private String id;
    private String name;
    private String ownerId;
    private String ownerName;
    private List<Member> members;

    public DiscordGuild(String id, String name, String ownerId, String ownerName) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.members = new ArrayList<>();
    }

}
