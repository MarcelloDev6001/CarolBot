package com.marc.discordbot.carol.database.entities.guild;

public class CarolDatabaseGuildChannelSettings {
    private long id = 0L;
    private boolean linksAllowed = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLinksAllowed() {
        return linksAllowed;
    }

    public void setLinksAllowed(boolean linksAllowed) {
        this.linksAllowed = linksAllowed;
    }

    public CarolDatabaseGuildChannelSettings(long id)
    {
        setId(id);
    }
}
