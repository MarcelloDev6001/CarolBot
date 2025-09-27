package com.marc.discordbot.carol.listeners.interaction;

import com.marc.discordbot.carol.commands.CarolBaseCommandOption;
import com.marc.discordbot.carol.commands.CarolCommand;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Collectors;

public class CarolCommandAutoCompleteInteractionListener extends ListenerAdapter { // why those things use huge names? ;-;
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        for (CarolCommand command : CarolCommand.allCommands) {
            if (command.getOptions() == null)
            {
                continue;
            }
            for (CarolBaseCommandOption option : command.getOptions()) {
                if (event.getName().equals(command.getName())) {
                    assert option != null;
                    if (event.getFocusedOption().getName().equals(option.getName()) && option.getAutoComplete() != null) {
                        String input = event.getFocusedOption().getValue();
                        List<String> suggestions = option.getAutoComplete();

                        List<Command.Choice> choices = suggestions.stream()
                                .filter(s -> s.startsWith(input.toLowerCase()))
                                .map(s -> new Command.Choice(s, s))
                                .collect(Collectors.toList());

                        event.replyChoices(choices).queue();
                    }
                }
            }
        }
    }
}
