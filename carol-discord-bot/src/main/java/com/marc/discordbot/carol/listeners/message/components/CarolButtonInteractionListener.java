package com.marc.discordbot.carol.listeners.message.components;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.messages.components.button.CarolBaseMessageButton;
import com.marc.discordbot.carol.messages.components.CarolMessageComponentsManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        for (CarolBaseMessageButton messageButton : CarolMessageComponentsManager.cachedMessageButtons)
        {
            if (messageButton.uniqueId.equals(event.getComponentId()) && messageButton.onClicked != null)
            {
                messageButton.onClicked.run(event.getInteraction(), event.getUser());
                return;
            }
        }

        event.reply(CarolSettings.MESSAGE_ON_BUTTON_AFTER_UNCACHE).setEphemeral(true).queue();
    }
}
