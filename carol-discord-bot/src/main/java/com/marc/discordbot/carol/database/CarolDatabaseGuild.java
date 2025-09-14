package com.marc.discordbot.carol.database;

import java.util.HashMap;
import java.util.Map;

public class CarolDatabaseGuild {
    private long id = 0L;
    private long new_member_warn_channel_id = 0L;
    private long member_left_warn_channel_id = 0L;

    public CarolDatabaseGuild() {}

    public static CarolDatabaseGuild getDefault(long id)
    {
        CarolDatabaseGuild carolDatabaseGuild = new CarolDatabaseGuild();
        carolDatabaseGuild.setId(id);
        carolDatabaseGuild.setNew_member_warn_channel_id(0);
        carolDatabaseGuild.setMember_left_warn_channel_id(0);
        return carolDatabaseGuild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNew_member_warn_channel_id() {
        return new_member_warn_channel_id;
    }

    public void setNew_member_warn_channel_id(long new_member_warn_channel_id) {
        this.new_member_warn_channel_id = new_member_warn_channel_id;
    }

    public long getMember_left_warn_channel_id() {
        return member_left_warn_channel_id;
    }

    public void setMember_left_warn_channel_id(long member_left_warn_channel_id) {
        this.member_left_warn_channel_id = member_left_warn_channel_id;
    }
}