package com.marc.discordbot.carol.messages.components.dropdown;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.messages.components.CarolMessageComponentsManager;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarolStringSelectMenu {
    private final StringSelectMenu menu;
    private final List<CarolBaseDropdownMenuOption> options;

    // why using byte instead of int on minValue and maxValue? simply, just to economize memory
    public CarolStringSelectMenu(String id, String placeholder, @NotNull List<CarolBaseDropdownMenuOption> options, byte minValue, byte maxValue)
    {
        StringSelectMenu.Builder builder = StringSelectMenu.create(id)
                .setPlaceholder(placeholder)
                .setMinValues(minValue)
                .setMaxValues(maxValue);

        for (CarolBaseDropdownMenuOption option : options)
        {
            builder.addOption(option.label(), option.value(), option.description());
        }
        this.options = options;
        menu = builder.build();

        CarolMessageComponentsManager.cachedStringMenus.add(this);
        uncacheWithDelay();
    }

    public StringSelectMenu toSelectMenu()
    {
        return menu;
    }

    public List<CarolBaseDropdownMenuOption> getOptions()
    {
        return options;
    }

    public void uncacheWithDelay()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            CarolMessageComponentsManager.cachedStringMenus.remove(this);
        }, CarolSettings.MAX_CACHE_TIME_FOR_MESSAGE_BUTTONS, TimeUnit.SECONDS);

        scheduler.schedule(scheduler::shutdown, 0, TimeUnit.SECONDS);
    }
}
