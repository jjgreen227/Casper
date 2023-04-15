package dev.casperbot.listeners;

import dev.casperbot.*;
import dev.casperbot.util.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.unions.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.managers.*;

import java.util.*;

public class VoiceChannelListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (!event.getMember().getId().equalsIgnoreCase("793293109034876978")) return;
        if (event.getNewValue() == null) {
            leaveChannel();
            return;
        }
        GuildVoiceState voiceState = event.getEntity().getVoiceState();
        if (voiceState == null) return;
        AudioChannelUnion union = event.getChannelJoined();
        if (union == null) return;
        VoiceChannel channel = union.asVoiceChannel();
        joinVoiceChannel(channel);
    }

    private void joinVoiceChannel(VoiceChannel channel) {
        Guild guild = Main.api.getGuildById(CasperConstants.GUILD_ID);
        AudioManager audioManager = Objects.requireNonNull(guild).getAudioManager();
        audioManager.openAudioConnection(channel);
    }

    private void leaveChannel() {
        Guild guild = Main.api.getGuildById(CasperConstants.GUILD_ID);
        AudioManager audioManager = Objects.requireNonNull(guild).getAudioManager();
        audioManager.closeAudioConnection();
    }
}
