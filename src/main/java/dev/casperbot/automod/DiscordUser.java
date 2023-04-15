package dev.casperbot.automod;

import lombok.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@Getter
@Setter
public class DiscordUser {

    private String name;
    private String id;
    private List<Role> roles;
    private boolean isMuted, isTimed, isBanned;

    public DiscordUser(String id, String name) {
        this.name = name;
        this.id = id;
        this.roles = new ArrayList<>();
        this.isMuted = false;
        this.isTimed = false;
        this.isBanned = false;
    }

    public DiscordUser(String id) {
        this(id, "");
        this.roles = new ArrayList<>();
        this.isMuted = false;
        this.isTimed = false;
        this.isBanned = false;
    }

    public void info(SlashCommandInteractionEvent event, Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        Random random = new Random();
        int randomColor = random.nextInt();
        Color color = new Color(randomColor);
        if (member != null) {
            builder.setColor(color);
            builder.setTitle("User Information");
            builder.addField("User ID", member.getId(), true);
            builder.addField("Administrator", member.hasPermission(Permission.ADMINISTRATOR) ? "Yes" : "No", false);
            builder.addField("Roles", getFormattedRoles(member), true);
            builder.addField("Joined Server", member.getTimeJoined().toString(), true);
            builder.addField("Permissions", getFormattedPermissions(member), false);
            builder.setImage(member.getEffectiveAvatarUrl());
            event.replyEmbeds(builder.build()).queue();
        }
    }

    public void showCache(SlashCommandInteractionEvent event) {
        // Cached users go here.

        event.reply("Cached Users: ").setEphemeral(true).queue();
    }

    private String getFormattedPermissions(Member member) {
        StringBuilder stringBuilder = new StringBuilder();
        int finish = member.getPermissions().size();
        int currentIndex = 0;
        for (Permission permission : member.getPermissions()) {
            if (finish - 1 < currentIndex || currentIndex == 0) {
                stringBuilder.append(permission.getName());
            } else {
                stringBuilder.append(", ").append(permission.getName());
            }
            currentIndex++;
        }
        return stringBuilder.toString();
    }

    private String getFormattedRoles(Member member) {
        StringBuilder stringBuilder = new StringBuilder();
        int finish = member.getRoles().size();
        int currentIndex = 0;
        for (Role role : member.getRoles()) {
            if (finish - 1 < currentIndex || currentIndex == 0) {
                stringBuilder.append(role.getAsMention());
            } else {
                stringBuilder.append(", ").append(role.getAsMention());
            }
            currentIndex++;
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final DiscordUser that)) return false;
        return isMuted() == that.isMuted() && isTimed() == that.isTimed() && isBanned() == that.isBanned() && Objects.equals(getName(), that.getName()) && Objects.equals(getId(), that.getId()) && Objects.equals(getRoles(), that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getRoles(), isMuted(), isTimed(), isBanned());
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", roles=" + roles +
                ", isMuted=" + isMuted +
                ", isTimed=" + isTimed +
                ", isBanned=" + isBanned +
                '}';
    }
}
