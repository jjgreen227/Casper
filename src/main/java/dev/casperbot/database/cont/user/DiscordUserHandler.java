package dev.casperbot.database.cont.user;

import dev.casperbot.util.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.*;

import java.time.format.*;

public class DiscordUserHandler extends Handler {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final Member member = event.getMember();
        CasperUser user = new CasperUser(member.getId(), member.getEffectiveName());
        user.setBanned(false);
        user.setTimed(false);
        user.setRoles(member.getRoles());
        user.setJoinedDate(member.getTimeJoined().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        CasperUserManager manager = CasperUserManager.getInstance(user);
        manager.addDiscordUser(user.getId(), user.getName());
    }
}
