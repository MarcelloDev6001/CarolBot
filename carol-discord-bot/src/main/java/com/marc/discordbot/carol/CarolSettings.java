package com.marc.discordbot.carol;

public class CarolSettings {
    // this is used for CarolBaseMessageButton.uncacheWithDelay()
    public static final long MAX_CACHE_TIME_FOR_MESSAGE_BUTTONS = 300;

    // used on CarolGuildJoinListener.onGuildJoin()
    public static final String DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD = "Howdy!\nMeu nome é Carol, obrigado por me adicionarem nesse servidor!\nObrigado ${owner}";

    // used on CarolDatabaseManager.initialize()
    public static final String FIREBASE_KEY_PATH = "src/main/resources/firebase-key.json";
}
