package com.marc.discordbot.carol.database.entities;

import java.util.List;

public class CarolDatabaseGuild {
    private long id = 0L;
    private List<CarolDatabaseGuildChannelSettings> channelsSettings = List.of();
    private List<CarolDatabaseGuildRoleSettings> rolesSettings = List.of();
    private List<CarolDatabaseGuildCommandSettings> commandsSettings = List.of();

    public CarolDatabaseGuild() {}

    public static CarolDatabaseGuild getDefault(long id)
    {
        CarolDatabaseGuild carolDatabaseGuild = new CarolDatabaseGuild();
        carolDatabaseGuild.setId(id);
        carolDatabaseGuild.setChannelsSettings(List.of());
        carolDatabaseGuild.setRolesSettings(List.of());
        carolDatabaseGuild.setCommandsSettings(List.of());
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
}