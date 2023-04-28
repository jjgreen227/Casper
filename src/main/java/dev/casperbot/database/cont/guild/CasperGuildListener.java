package dev.casperbot.database.cont.guild;

import dev.casperbot.util.*;
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
        CasperConstants.fine("Joined guild " + name + " (" + id + ")");
        createLogs(guild);
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        String id = guild.getId();
        String name = guild.getName();
        CasperGuildManager.getInstance().removeGuild(id);
        CasperConstants.fine("Left guild " + name + " (" + id + ")");
    }

    private void createLogs(Guild guild) {
        CasperGuild casperGuild = CasperGuildManager.getInstance().getGuild(guild.getId());
        if (casperGuild == null) return;
        if (guild.getCategoriesByName("test", true).isEmpty()) {
            CasperConstants.severe("Unable to find support category. Creating category...");
            guild.createCategory("test").queue();
            CasperConstants.fine("Category has been created.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CasperConstants.warning("Creating channels within category...");
            guild.getCategoriesByName("test", true).forEach(category -> {
                category.createTextChannel("audit-log").setTopic("Logging Moderation Actions.").queue();
                category.createTextChannel("changelog").setTopic("Logging GitHub Actions.").queue();
            });
            CasperConstants.fine("Channels created.");
        } else {
            CasperConstants.fine("Category exists.");
        }
    }


}
