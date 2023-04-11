package dev.casperbot.automod;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.*;

import java.util.*;

public class DiscordUserFactory extends ListenerAdapter {
    private Map<String, DiscordUser> discordUserMap = new HashMap<>();

    public DiscordUserFactory() {
        this.discordUserMap = new HashMap<>();
    }

    public DiscordUserFactory(DiscordUser user) {
        this.discordUserMap = new HashMap<>();
        addDiscordUser(user.getId(), user);
    }

    public void addDiscordUser(String id, DiscordUser user) {
        this.discordUserMap.put(id, user);
    }

    public void removeDiscordUserCache(DiscordUser user) {
        this.discordUserMap.remove(user);
    }

    public DiscordUser getUserId(String id) {
        return this.discordUserMap.get(new DiscordUser(id));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final Member member = event.getMember();
        DiscordUser dUser = new DiscordUser(member.getId());
        addDiscordUser(member.getId(), dUser);
    }
}
