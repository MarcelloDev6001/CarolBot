package com.marc.discordbot.carol.database.entities.guild;

public class CarolDatabaseGuildChannelSettings {
    private String id = "";
    private boolean linksAllowed = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLinksAllowed() {
        return linksAllowed;
    }

    public void setLinksAllowed(boolean linksAllowed) {
        this.linksAllowed = linksAllowed;
    }

    public CarolDatabaseGuildChannelSettings(String id)
    {
        setId(id);
    }
}
