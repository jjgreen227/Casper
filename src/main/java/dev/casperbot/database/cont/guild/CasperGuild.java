package dev.casperbot.database.cont.guild;

import dev.casperbot.util.*;
import lombok.*;
import net.dv8tion.jda.api.events.interaction.command.*;

import java.lang.reflect.*;
import java.util.*;

@Getter
@Setter
public class CasperGuild {

    private String id;
    private String name;
    private String ownerId;
    private String ownerName;
    private int memberSize;
    private List<Member> members;

    public CasperGuild(String id, String name, String ownerId, String ownerName, int members) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.memberSize = members;
    }

    public void info(SlashCommandInteractionEvent event) {
        EmbedUtil.guildInfoEmbed(event.getGuildChannel().asTextChannel(), this.name, this.id, this.ownerName, this.memberSize);
    }
}
