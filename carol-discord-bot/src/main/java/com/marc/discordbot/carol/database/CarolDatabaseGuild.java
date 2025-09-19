package com.marc.discordbot.carol.database;

public class CarolDatabaseGuild {
    private long id = 0L;
    private long memberJoinedChannelWarnID = 0L;
    private long memberLeftChannelWarnID = 0L;

    public CarolDatabaseGuild() {}

    public static CarolDatabaseGuild getDefault(long id)
    {
        CarolDatabaseGuild carolDatabaseGuild = new CarolDatabaseGuild();
        carolDatabaseGuild.setId(id);
        carolDatabaseGuild.setMemberJoinedChannelWarnID(0);
        carolDatabaseGuild.setMemberLeftChannelWarnID(0);
        return carolDatabaseGuild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberJoinedChannelWarnID() {
        return memberJoinedChannelWarnID;
    }

    public void setMemberJoinedChannelWarnID(long memberJoinedChannelWarnID) {
        this.memberJoinedChannelWarnID = memberJoinedChannelWarnID;
    }

    public long getMemberLeftChannelWarnID() {
        return memberLeftChannelWarnID;
    }

    public void setMemberLeftChannelWarnID(long memberLeftChannelWarnID) {
        this.memberLeftChannelWarnID = memberLeftChannelWarnID;
    }
}