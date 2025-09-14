package com.marc.discordbot.carol.messages.components;

import com.marc.discordbot.carol.CarolSettings;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarolBaseMessageButton
{
    public final ButtonStyle style;
    public final String uniqueId;
    public final String label;
    @Nullable public final CarolBaseMessageButtonOnClickEvent onClicked;
    public @Nullable String link;

    public Button toComponentButton() {
        switch (style) {
            case ButtonStyle.SECONDARY -> {
                return Button.secondary(uniqueId, label);
            }
            case ButtonStyle.DANGER -> {
                return Button.danger(uniqueId, label);
            }
            case ButtonStyle.LINK -> {
                return Button.link((link != null) ? link : "", label);
            }
            case ButtonStyle.SUCCESS -> {
                return Button.success(uniqueId, label);
            }
            default -> { // Primary, Unknown or Premium (Premium will be added later)
                return Button.primary(uniqueId, label);
            }
        }
    }

    public void uncacheWithDelay()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            CarolMessageComponentsManager.cachedButtons.remove(this);
        }, CarolSettings.MAX_CACHE_TIME_FOR_MESSAGE_BUTTONS, TimeUnit.SECONDS);

        scheduler.schedule(scheduler::shutdown, 0, TimeUnit.SECONDS);
    }

    public CarolBaseMessageButton(ButtonStyle style, String uniqueId, String label, CarolBaseMessageButtonOnClickEvent onClicked, @Nullable String link) {
        this.style = style;
        this.uniqueId = uniqueId;
        this.label = label;
        this.onClicked = onClicked;
        this.link = link;

        CarolMessageComponentsManager.cachedButtons.add(this);
        uncacheWithDelay();
    }
}
