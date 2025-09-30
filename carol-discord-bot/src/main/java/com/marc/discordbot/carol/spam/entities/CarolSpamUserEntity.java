package com.marc.discordbot.carol.spam.entities;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class CarolSpamUserEntity {
    public long id = 0L;
    public List<Message> messages = new ArrayList<>();
}
