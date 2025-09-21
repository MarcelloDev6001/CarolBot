package com.marc.discordbot.carol.listeners;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.messages.components.CarolMessageComponentsManager;
import com.marc.discordbot.carol.messages.components.dropdown.CarolBaseDropdownMenuOption;
import com.marc.discordbot.carol.messages.components.dropdown.CarolStringSelectMenu;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolOnStringSelectInteractionListener extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        for (CarolStringSelectMenu selectMenu : CarolMessageComponentsManager.cachedStringMenus)
        {
            for (CarolBaseDropdownMenuOption option : selectMenu.getOptions())
            {
                if (option.onClicked() != null)
                {
                    option.onClicked().run(event.getInteraction(), event.getUser(), event.getValues());
                    return;
                }
            }
        }

        event.reply(CarolSettings.MESSAGE_ON_BUTTON_AFTER_UNCACHE).setEphemeral(true).queue();
    }
}
