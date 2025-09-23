package com.marc.discordbot.carol.achievements;

public class CarolAchievementObject {
    public long id = 0L;
    public String name = "";
    public String description = "";
    public String requirement = "";
    public boolean achievable = true;
    public int difficulty = 0; // 0 - 5

    public CarolAchievementObject(long id, String name, String description, String requirement, boolean achievable)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requirement = requirement;
        this.achievable = achievable;
        difficulty = 0;
    }

    public CarolAchievementObject(long id, String name, String description, String requirement, boolean achievable, int difficulty)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requirement = requirement;
        this.achievable = achievable;
        this.difficulty = difficulty;
    }
}
