package com.marc.discordbot.carol;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class CarolSettings {
    // this is used for CarolBaseMessageButton.uncacheWithDelay()
    public static final long MAX_CACHE_TIME_FOR_MESSAGE_BUTTONS = 300;

    // both used on CarolGuildJoinListener.onGuildJoin()
    public static final String DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD = "Howdy!\nMeu nome é Carol, obrigado por me adicionarem nesse servidor!\nObrigado ${owner}";
    public static final String DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD_WITHOUT_OWNER_MENTION = "Howdy!\nMeu nome é Carol, obrigado por me adicionarem nesse servidor!";

    // used on CarolDatabaseManager.initialize()
    public static final String FIREBASE_KEY_PATH = "src/main/resources/firebase-key.json";

    // those are used on CarolLauncher.initDiscordActivity()
    public static final OnlineStatus ONLINE_STATUS = OnlineStatus.ONLINE;
    public static final ActivityType ACTIVITY_TYPE = ActivityType.WATCHING;
    public static final String ACTIVITY_INFO = "Loritta Massacre 3000";
}
