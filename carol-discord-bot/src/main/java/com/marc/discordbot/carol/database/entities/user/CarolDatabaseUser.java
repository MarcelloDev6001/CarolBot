package com.marc.discordbot.carol.database.entities.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarolDatabaseUser {
    private long id = 0L;
    private int money = 0;
    private Map<String, Integer> xpInGuilds = new HashMap<String, Integer>();
    private List<CarolDatabaseUserAchievement> achievements = new ArrayList<>();

    // heh... a marriage system just like Loritta :3
    // why does "Cônjuge" (PT-BR) is translated to "Spouse" in EN-US? this doesn't make sense for me ;-;
    private String spouseID = "0";

    public CarolDatabaseUser() {}

    public static CarolDatabaseUser getDefault(long id)
    {
        CarolDatabaseUser carolDatabaseUser = new CarolDatabaseUser();
        carolDatabaseUser.setId(id);
        carolDatabaseUser.setMoney(0);
        carolDatabaseUser.setXpInGuilds(new HashMap<String, Integer>());
        return carolDatabaseUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Map<String, Integer> getXpInGuilds() {
        return xpInGuilds;
    }

    public void setXpInGuilds(Map<String, Integer> xpInGuilds) {
        this.xpInGuilds = xpInGuilds;
    }

    public List<CarolDatabaseUserAchievement> getAchievements() { return achievements; }

    public void setAchievements(List<CarolDatabaseUserAchievement> achievements) { this.achievements = achievements; }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}