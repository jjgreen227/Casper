package dev.casperbot.database.cont.user;

import lombok.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.*;

import java.awt.*;
import java.time.format.*;
import java.util.*;
import java.util.List;

@Getter
@Setter
public class CasperUser {

    private String name;
    private String id;
    private List<Role> roles;
    private boolean isAdmin, isTimed, isBanned;
    private String joinedDate;

    public CasperUser(String id, String name) {
        this.name = name;
        this.id = id;
        this.roles = new ArrayList<>();
        this.isAdmin = false;
        this.isTimed = false;
        this.isBanned = false;
        this.joinedDate = String.valueOf(new Date(DateTimeFormatter.ofPattern("MM/dd/yyyy").format(new Date().toInstant()))); // Amurica format lol...
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
        if (!(o instanceof final CasperUser user)) return false;
        return isAdmin() == user.isAdmin() && isTimed() == user.isTimed() && isBanned() == user.isBanned() && Objects.equals(getName(), user.getName()) && Objects.equals(getId(), user.getId()) && Objects.equals(getRoles(), user.getRoles()) && Objects.equals(getJoinedDate(), user.getJoinedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getRoles(), isAdmin(), isTimed(), isBanned(), getJoinedDate());
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", roles=" + roles +
                ", isAdmin=" + isAdmin +
                ", isTimed=" + isTimed +
                ", isBanned=" + isBanned +
                ", joinedDate='" + joinedDate + '\'' +
                '}';
    }
}
