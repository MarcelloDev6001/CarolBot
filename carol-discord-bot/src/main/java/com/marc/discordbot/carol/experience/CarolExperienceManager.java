package com.marc.discordbot.carol.experience;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.CarolDatabaseUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.util.Map;

public class CarolExperienceManager {
    public static void updateUserXPFromGuild(User user, Guild guild) throws IOException, InterruptedException {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getIdLong());
        if (dbUser == null) { dbUser = CarolDatabaseUser.getDefault(user.getIdLong()); }
        Map<String, Integer> guildsXPs = dbUser.getXp_in_guilds();

        guildsXPs.putIfAbsent(guild.getId(), 0);
        guildsXPs.put(guild.getId(), guildsXPs.get(guild.getId()) + 1);
        dbUser.setXp_in_guilds(guildsXPs);

        CarolDatabaseManager.updateUser(user.getIdLong(), dbUser);
    }
}
