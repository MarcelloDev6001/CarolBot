package com.marc.discordbot.carol.cache;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
