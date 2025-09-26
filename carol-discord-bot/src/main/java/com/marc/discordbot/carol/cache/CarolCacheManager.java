package com.marc.discordbot.carol.cache;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// soo, I'll try to explain how this works:
// when I try to pull a guild from the database, everytime is a new read, and this will consume alot of read
// so to reduce read, we cache the guild and user, and then we update it every X minutes
// when you try to pull using any get method from CarolDatabaseManager, it firstly tries to get from the cache
// if not found, then it gets from the database
// IDK if this is a good system, but I hope it is
public class CarolCacheManager {
    public static void initialize() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            try {
                for (CarolDatabaseUser dbUserCached : CarolDatabaseCache.dbUsersCached) {
                    if (dbUserCached != null) {
                        CarolDatabaseManager.updateUserOnDatabase(dbUserCached.getId(), dbUserCached);
                    }
                }
                CarolDatabaseCache.dbUsersCached.clear();

                for (CarolDatabaseGuild dbGuildCached : CarolDatabaseCache.dbGuildsCached) {
                    if (dbGuildCached != null) {
                        CarolDatabaseManager.updateGuildOnDatabase(dbGuildCached.getId(), dbGuildCached);
                    }
                }
                CarolDatabaseCache.dbGuildsCached.clear();

                // System.out.println("Database updated!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        scheduler.scheduleAtFixedRate(task, CarolSettings.PERIOD_OF_UPDATING_DATABASE, CarolSettings.PERIOD_OF_UPDATING_DATABASE, TimeUnit.MINUTES);
    }
}
