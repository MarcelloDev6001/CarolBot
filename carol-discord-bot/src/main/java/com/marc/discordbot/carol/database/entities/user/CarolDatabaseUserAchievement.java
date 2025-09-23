package com.marc.discordbot.carol.database.entities.user;

import com.google.cloud.Timestamp;

public class CarolDatabaseUserAchievement {
    private long id = 0L;
    private Timestamp awardedDate = null;

    public CarolDatabaseUserAchievement() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getAwardedDate() {
        return awardedDate;
    }

    public void setAwardedDate(Timestamp awardedDate) {
        this.awardedDate = awardedDate;
    }
}
