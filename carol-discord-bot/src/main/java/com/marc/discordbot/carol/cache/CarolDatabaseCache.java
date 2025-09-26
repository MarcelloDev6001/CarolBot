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

// soo, I'll try to explain how this works:
// when I try to pull a guild from the database, everytime is a new read, and this will consume alot of read
// so to reduce read, we cache the guild and user, and then we update it every X minutes
// when you try to pull using any get method from CarolDatabaseManager, it firstly tries to get from the cache
// if not found, then it gets from the database
// IDK if this is a good system, but i hope it is
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
