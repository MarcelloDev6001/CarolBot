package com.marc.discordbot.carol.cache;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import com.marc.discordbot.carol.messages.components.CarolMessageComponentsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarolDatabaseCache {
    public static List<CarolDatabaseUser> dbUsersCached = new ArrayList<>();
    public static List<CarolDatabaseGuild> dbGuildsCached = new ArrayList<>();

    public static void cacheUser(CarolDatabaseUser dbUser)
    {
        dbUsersCached.remove(dbUser);
        dbUsersCached.add(dbUser);
    }

    public static void cacheGuild(CarolDatabaseGuild dbGuild)
    {
        dbGuildsCached.remove(dbGuild);
        dbGuildsCached.add(dbGuild);
    }
}
