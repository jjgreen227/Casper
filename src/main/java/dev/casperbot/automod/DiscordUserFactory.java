package dev.casperbot.automod;

import dev.casperbot.database.*;
import dev.casperbot.util.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.*;

import java.util.*;

public class DiscordUserFactory extends ListenerAdapter {
    private final Map<String, String> discordUserMap;

    public DiscordUserFactory() {
        this.discordUserMap = new HashMap<>();
    }

    public DiscordUserFactory(DiscordUser user) {
        this.discordUserMap = new HashMap<>();
    }

    public void addDiscordUser(String id, String name) {
        this.discordUserMap.put(id, name);
        MySQLFactory.addDiscordUser(id, name);
        CasperConstants.fine("Added user " + name + " to the database.");
    }

    public void removeDiscordUserCache(DiscordUser user) {
        this.discordUserMap.remove(user);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final Member member = event.getMember();
        DiscordUser user = new DiscordUser(member.getId(), member.getEffectiveName());
        addDiscordUser(user.getId(), user.getName());
    }
}
