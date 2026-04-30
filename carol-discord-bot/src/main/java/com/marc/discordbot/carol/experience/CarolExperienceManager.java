package com.marc.discordbot.carol.experience;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.util.Map;

public class CarolExperienceManager {
    public static void updateUserXPFromGuild(User user, Guild guild) throws IOException, InterruptedException {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getId());

        if (dbUser == null)
        {
            dbUser = CarolDatabaseUser.getDefault(user.getId());
        }
        Map<String, Integer> guildsXPs = dbUser.getXpInGuilds();

        String guildId = guild.getId();

        guildsXPs.putIfAbsent(guildId, 0);
        guildsXPs.put(guildId, guildsXPs.get(guildId) + 1);
        dbUser.setXpInGuilds(guildsXPs);

        CarolDatabaseManager.updateUser(user.getId(), dbUser);
    }

    public static int getUserXPFromGuild(User user, Guild guild)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getId());
        if (dbUser == null) { dbUser = CarolDatabaseUser.getDefault(user.getId()); }
        Map<String, Integer> guildsXPs = dbUser.getXpInGuilds();

        return guildsXPs.getOrDefault(guild.getId(),0);
    }
}
