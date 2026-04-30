package com.marc.discordbot.carol.economy;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import net.dv8tion.jda.api.entities.User;

public class CarolEconomyManager {
    public static int getUserMoney(User user)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getId());
        return dbUser.getMoney();
    }
}
