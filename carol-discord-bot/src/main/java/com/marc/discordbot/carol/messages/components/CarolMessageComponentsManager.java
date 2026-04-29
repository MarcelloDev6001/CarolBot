package com.marc.discordbot.carol.messages.components;

import com.marc.discordbot.carol.messages.components.button.CarolBaseMessageButton;
import com.marc.discordbot.carol.messages.components.dropdown.CarolStringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public class CarolMessageComponentsManager {
    public static List<CarolBaseMessageButton> cachedMessageButtons = new ArrayList<>();
    public static List<CarolStringSelectMenu> cachedStringMenus = new ArrayList<>();
}
