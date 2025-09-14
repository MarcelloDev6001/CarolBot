package com.marc.discordbot.carol.database;

import java.util.HashMap;
import java.util.Map;

public class CarolDatabaseUser {
    private long id = 0L;
    private int money = 0;
    private Map<String, Integer> xp_in_guilds = new HashMap<String, Integer>();

    public CarolDatabaseUser(long id, int money, Map<String, Integer> xp_in_guilds)
    {
        this.id = id;
        this.money = money;
        this.xp_in_guilds = xp_in_guilds;
    }

    public static CarolDatabaseUser getDefault(long id)
    {
        return new CarolDatabaseUser(id, 0, new HashMap<String, Integer>());
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

    public Map<String, Integer> getXp_in_guilds() {
        return xp_in_guilds;
    }

    public void setXp_in_guilds(Map<String, Integer> xp_in_guilds) {
        this.xp_in_guilds = xp_in_guilds;
    }
}