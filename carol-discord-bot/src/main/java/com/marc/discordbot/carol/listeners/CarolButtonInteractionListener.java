package com.marc.discordbot.carol.listeners;

import com.marc.discordbot.carol.messages.components.CarolBaseMessageButton;
import com.marc.discordbot.carol.messages.components.CarolMessageComponentsManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        for (CarolBaseMessageButton messageButton : CarolMessageComponentsManager.cachedButtons)
        {
            if (messageButton.uniqueId.equals(event.getComponentId()))
            {
                messageButton.onClicked.run(event.getInteraction());
                return;
            }
        }

        event.reply(
                "Parece que os dados de interação dessa mensagem se perderam!\nCrie uma nova interação para usar esses botões novamente"
        ).setEphemeral(true).queue();
    }
}
