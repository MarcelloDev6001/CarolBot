package com.marc.discordbot.carol.database.entities.guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarolDatabaseGuild {
    private long id = 0L;
    private List<CarolDatabaseGuildChannelSettings> channelsSettings = List.of();
    private List<CarolDatabaseGuildRoleSettings> rolesSettings = List.of();
    private List<CarolDatabaseGuildCommandSettings> commandsSettings = List.of();
    private List<String> unauthorizedWords = List.of();
    private Map<Long, Integer> xpMultipliersOfRoles = new HashMap<>(); // Long = ID of the Role; Integer = the xp modifier
    private boolean achievementsAllowed = true;

    public CarolDatabaseGuild() {}

    public static CarolDatabaseGuild getDefault(long id)
    {
        CarolDatabaseGuild carolDatabaseGuild = new CarolDatabaseGuild();
        carolDatabaseGuild.setId(id);
        carolDatabaseGuild.setChannelsSettings(List.of());
        carolDatabaseGuild.setRolesSettings(List.of());
        carolDatabaseGuild.setCommandsSettings(List.of());
        carolDatabaseGuild.setUnauthorizedWords(List.of());
        carolDatabaseGuild.setXpMultipliersOfRoles(new HashMap<>());
        carolDatabaseGuild.setAchievementsAllowed(true);
        return carolDatabaseGuild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CarolDatabaseGuildChannelSettings> getChannelsSettings() {
        return channelsSettings;
    }

    public void setChannelsSettings(List<CarolDatabaseGuildChannelSettings> channelsSettings) {
        this.channelsSettings = channelsSettings;
    }

    public List<CarolDatabaseGuildRoleSettings> getRolesSettings() {
        return rolesSettings;
    }

    public void setRolesSettings(List<CarolDatabaseGuildRoleSettings> rolesSettings) {
        this.rolesSettings = rolesSettings;
    }

    public List<CarolDatabaseGuildCommandSettings> getCommandsSettings() {
        return commandsSettings;
    }

    public void setCommandsSettings(List<CarolDatabaseGuildCommandSettings> commandsSettings) {
        this.commandsSettings = commandsSettings;
    }

    public boolean isAchievementsAllowed() {
        return achievementsAllowed;
    }

    public void setAchievementsAllowed(boolean achievementsAllowed) {
        this.achievementsAllowed = achievementsAllowed;
    }

    public Map<Long, Integer> getXpMultipliersOfRoles() {
        return xpMultipliersOfRoles;
    }

    public void setXpMultipliersOfRoles(Map<Long, Integer> xpMultipliersOfRoles) {
        this.xpMultipliersOfRoles = xpMultipliersOfRoles;
    }

    public List<String> getUnauthorizedWords() {
        return unauthorizedWords;
    }

    public void setUnauthorizedWords(List<String> unauthorizedWords) {
        this.unauthorizedWords = unauthorizedWords;
    }
}