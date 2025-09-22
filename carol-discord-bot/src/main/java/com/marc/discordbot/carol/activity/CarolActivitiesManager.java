package com.marc.discordbot.carol.activity;

import com.marc.discordbot.carol.CarolSettings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarolActivitiesManager {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static Activity[] dailyActivitiesList = new Activity[]{
            Activity.watching("A morte da Loritta"),
            Activity.playing("Roblox"),
            Activity.listening("Kasane Teto"),
            Activity.listening("Hatsune Miku"),
            Activity.playing("Loritta Massacre 3000"),
            Activity.customStatus("Nada aqui por enquanto...")
    };

    public static void executeEveryMidNight(Runnable task) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.toLocalDate().atStartOfDay().plusDays(1);

        long initialDelay = Duration.between(now, nextRun).toMillis();
        long period = TimeUnit.DAYS.toMillis(1);

        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static void setRandomActivity(Presence jdaPresences)
    {
        Activity randomActivity = dailyActivitiesList[(int)(Math.random() * dailyActivitiesList.length)];
        jdaPresences.setPresence(CarolSettings.ONLINE_STATUS, randomActivity);
    }

    public static void initDiscordActivity(JDA jda)
    {
        Presence jdaPresences = jda.getPresence();

        if (CarolSettings.ACTIVITY_TYPE != null && CarolSettings.ACTIVITY_INFO != "") // predefined status = nonstatus changer
        {
            jdaPresences.setPresence(CarolSettings.ONLINE_STATUS, Activity.of(CarolSettings.ACTIVITY_TYPE, CarolSettings.ACTIVITY_INFO));
            return;
        }

        setRandomActivity(jdaPresences);

        if (CarolSettings.CHANGE_ACTIVITY_EVERY_DAY)
        {
            executeEveryMidNight(() -> {
                setRandomActivity(jdaPresences);
            });
        }
    }
}
