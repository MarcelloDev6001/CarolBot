package com.marc.discordbot.carol;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class CarolSettings {
    // used on CarolLauncher.buildJDA()
    // this is for manage how JDA will be built.
    // 0 = createDefault (Enables cache for users who are active in voice channels and all cache flags)
    // 1 = createLight (Disables all user cache and cache flags)
    // 2 = create (Enables member chunking, caches all users, and enables all cache flags)
    public static final byte JDA_BUILDER_TYPE = 0;

    // this is used for CarolBaseMessageButton.uncacheWithDelay()
    public static final long MAX_CACHE_TIME_FOR_MESSAGE_BUTTONS = 300;

    // both used on CarolGuildJoinListener.onGuildJoin()
    public static final String DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD = "Howdy!\nMeu nome é Carol, obrigado por me adicionarem nesse servidor!\nObrigado ${owner}";
    public static final String DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD_WITHOUT_OWNER_MENTION = "Howdy!\nMeu nome é Carol, obrigado por me adicionarem nesse servidor!";

    // used on CarolDatabaseManager.initialize()
    // leave it null if you want to put your Firebase-key.json content as an environment variable. (variable name needs to be FIREBASE_KEY)
    public static final String FIREBASE_KEY_PATH = "";

    // those are used on CarolActivitiesManager.initDiscordActivity()
    public static final OnlineStatus ONLINE_STATUS = OnlineStatus.ONLINE;
    public static final ActivityType ACTIVITY_TYPE = null; // leave this and ACTIVITY_INFO null and "" if you want to have a random activity when you start the bot.
    public static final String ACTIVITY_INFO = "";
    public static final boolean CHANGE_ACTIVITY_EVERY_DAY = true;

    // used on CarolButtonInteractionListener.onButtonInteraction()
    public static final String MESSAGE_ON_BUTTON_AFTER_UNCACHE = "Parece que os dados de interação dessa mensagem se perderam!\nCrie uma nova interação para usar esses botões/menus novamente";

    // both used on CarolCacheManager.initialize()
    public static final int PERIOD_OF_UPDATING_DATABASE_USERS = 300; // in seconds
    public static final int PERIOD_OF_UPDATING_DATABASE_GUILDS = 15;

    // used on CarolDashboardCommand.onCommandExecuted()
    public static final String DASHBOARD_URL = "https://caroldiscordbot.netlify.app";
}
