package com.marc.discordbot.carol.achievements;

import com.google.cloud.Timestamp;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUserAchievement;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.ArrayList;
import java.util.List;

public class CarolAchievementsManager {
    public static CarolAchievementObject[] achievementsList = new CarolAchievementObject[]{
            new CarolAchievementObject(0, "Teste", "Apenas um teste", "Use o comando de teste", true, 0)
    };

    public static List<CarolAchievementObject> getAchievementsObjectsFromUser(User user)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getIdLong());
        List<CarolAchievementObject> achList = new ArrayList<>(List.of());

        for (CarolAchievementObject achievement : achievementsList)
        {
            if (userHasAchievement(user, achievement.id))
            {
                achList.add(achievement);
            }
        }

        return achList;
    }

    public static List<Long> getAchievementsIDsFromUser(User user)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getIdLong());
        List<Long> idsList = new ArrayList<>(List.of());

        for (CarolDatabaseUserAchievement achievement : dbUser.getAchievements())
        {
            idsList.add(achievement.getId());
        }

        return idsList;
    }

    public static CarolAchievementObject getAchievementFromID(long achievementID)
    {
        for (CarolAchievementObject achievement : achievementsList)
        {
            if (achievement.id == achievementID)
            {
                return achievement;
            }
        }

        System.out.println("Achievement not found: " + achievementID);
        return null;
    }

    public static boolean userHasAchievement(User user, long achievementID)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getIdLong());

        boolean found = false;
        for (CarolAchievementObject achievement : achievementsList) { // firstly we verify if the achievement exists in achievementsList
            if (achievement.id == achievementID) {
                found = true;
                break;
            }
        }
        if (!found)
        {
            System.out.println("Achievement not found: " + achievementID);
            return false;
        }

        // and then we can award the achievement to user
        for (CarolDatabaseUserAchievement achievement : dbUser.getAchievements())
        {
            if (achievement.getId() == achievementID)
            {
                return true;
            }
        }

        return false;
    }

    public static void checkAndAwardAchievementToUser(User user, long achievementID)
    {
        if (!userHasAchievement(user, achievementID))
        {
            awardAchievementToUser(user, achievementID);
        }
    }

    public static void checkAndAwardAchievementToUser(User user, long achievementID, Message messageToReply)
    {
        if (!userHasAchievement(user, achievementID))
        {
            awardAchievementToUser(user, achievementID);
        }
        else
        {
            return;
        }

        CarolAchievementObject achievementObject = getAchievementFromID(achievementID);
        if (achievementObject == null) { return; }
        messageToReply.reply(
                "Conquista adquirida:\n# " + achievementObject.name + "\n" + achievementObject.description + "\n# Fácil, não acha?"
        ).queue();
    }

    public static void checkAndAwardAchievementToUser(User user, long achievementID, InteractionHook interactionHook)
    {
        if (!userHasAchievement(user, achievementID))
        {
            awardAchievementToUser(user, achievementID);
        }
        else
        {
            return;
        }

        CarolAchievementObject achievementObject = getAchievementFromID(achievementID);
        if (achievementObject == null) { return; }
        interactionHook.sendMessage(
                "Conquista adquirida:\n# " + achievementObject.name + "\n" + achievementObject.description + "\n# Fácil, não acha?"
        ).setEphemeral(true).queue();
    }

    private static void awardAchievementToUser(User user, long achievementID)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getIdLong());

        CarolDatabaseUserAchievement newAchievement = new CarolDatabaseUserAchievement();
        newAchievement.setId(achievementID);
        newAchievement.setAwardedDate(Timestamp.now());
        dbUser.getAchievements().add(newAchievement);

        CarolDatabaseManager.updateUser(user.getIdLong(), dbUser);
    }
}
