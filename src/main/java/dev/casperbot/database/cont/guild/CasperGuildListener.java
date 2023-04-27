package dev.casperbot.database.cont.guild;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.*;

import java.util.*;

public class CasperGuildListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        String id = guild.getId();
        String name = guild.getName();
        String ownerId = guild.getOwnerId();
        String ownerName = Objects.requireNonNull(guild.getOwner()).getEffectiveName();
        int memberCount = guild.getMemberCount();
        CasperGuildManager.getInstance().addGuild(id, name, ownerId, ownerName, memberCount);
    }
}
