package com.marc.discordbot.carol;

public class CarolProperties {
    private String token = "";
    private long applicationId = 0L;
    private String prefix = "";

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getApplicationId() { return applicationId; }
    public void setApplicationId(long applicationId) { this.applicationId = applicationId; }

    public String getPrefix() { return prefix; }
    public void  setPrefix(String prefix) { this.prefix = prefix; }
}