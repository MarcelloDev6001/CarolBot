package com.marc.discordbot.carol;

public class CarolProperties {
    private String token = "";
    private long applicationId = 0L;
    private String prefix = "";
    private String databaseURL = "";
    private String databaseUsername = "";
    private String databasePassword = "";
    private String databaseAnonKey = "";

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getApplicationId() { return applicationId; }
    public void setApplicationId(long applicationId) { this.applicationId = applicationId; }

    public String getPrefix() { return prefix; }
    public void  setPrefix(String prefix) { this.prefix = prefix; }

    public String getDatabaseURL() { return databaseURL; }
    public void setDatabaseURL(String databaseURL) { this.databaseURL = databaseURL; }

    public String getDatabaseUsername() { return databaseUsername; }
    public void setDatabaseUsername(String databaseUsername) { this.databaseUsername = databaseUsername; }

    public String getDatabasePassword() { return databasePassword; }
    public void setDatabasePassword(String databasePassword) { this.databasePassword = databasePassword; }

    public String getDatabaseAnonKey() { return databaseAnonKey; }
    public void setDatabaseAnonKey(String databaseAnonKey) { this.databaseAnonKey = databaseAnonKey; }
}