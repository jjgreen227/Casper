package dev.casperbot;

import lombok.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.components.buttons.*;

import java.util.*;

@Getter
public class RPS extends ListenerAdapter {

    // Make another button to start another game.
    // Delete the previous messages when another game starts.

    public void start(SlashCommandInteractionEvent event) {
        event.reply("Please select one of the following [Rock|Paper|Scissors]").addActionRow(
                        Button.primary("rock", "Rock"),
                        Button.primary("paper", "Paper"),
                        Button.primary("scissors", "Scissors"))
                .queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent buttonEvent) {
        Random random = new Random();
        String[] options = {"rock", "paper", "scissors"};
        String randomId = options[random.nextInt(options.length)];
        String componentId = buttonEvent.getComponentId();
        MessageHistory history = buttonEvent.getChannel().getHistory();
        switch (componentId) {
            case "rock" -> {
                if (componentId.equals(randomId)) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("We tied...play again!").queue();
                    return;
                }
                if (randomId.equals("paper")) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("I chose __Paper__. You chose __Rock__. You Lose!").queue();
                } else if (randomId.equals("scissors")) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("I chose __Scissors__. You chose __Rock__. You Win!").queue();
                }
            }
            case "paper" -> {
                if (componentId.equals(randomId)) {
                    buttonEvent.reply("Hey we're tied.").queue();
                    return;
                }
                if (randomId.equals("scissors")) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("I chose __Scissors__. You chose __Scissors__. You Lose!").queue();
                } else if (randomId.equals("rock")) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("I chose __Rock__. You chose __Paper__. You Win!").queue();
                }
            }
            case "scissors" -> {
                if (componentId.equals(randomId)) {
                    buttonEvent.reply("Hey we're tied.").queue();
                    return;
                }
                if (randomId.equals("rock")) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("I chose __Rock__. You chose __Scissors__. You Lose!").queue();
                } else if (randomId.equals("paper")) {
                    history.retrievePast(1).queue(messages -> buttonEvent.getChannel().purgeMessages(messages));
                    buttonEvent.reply("I chose __Paper__. You chose __Scissors__. You Win!").queue();
                }
            }
        }
    }
}
