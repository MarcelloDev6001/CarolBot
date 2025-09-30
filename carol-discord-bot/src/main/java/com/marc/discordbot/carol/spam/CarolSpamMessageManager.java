package com.marc.discordbot.carol.spam;

import com.marc.discordbot.carol.spam.entities.CarolSpamUserEntity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// calc for Messages Per Second: MESSAGE_AMOUNT / (
//  users.messages[users.messages.size() - 1].getTimeCreated().toInstant().toEpochMilli() - users.messages[0].getTimeCreated().toInstant().toEpochMilli()
// )
// if greater
public class CarolSpamMessageManager {
    public static List<CarolSpamUserEntity> users = new ArrayList<>();

    public static void startListenerForMessage(Message message, int maxSecondsToVerify)
    {
        CarolSpamUserEntity userEntityInList = null;
        for (CarolSpamUserEntity userEntity : users)
        {
            try {
                if (userEntity.id == message.getMember().getIdLong())
                {
                    userEntityInList = userEntity;
                }
            } catch (NullPointerException _) { return; }
        }
        if (userEntityInList == null)
        {
            userEntityInList = new CarolSpamUserEntity();
            try {
                userEntityInList.id = message.getMember().getIdLong();
                users.add(userEntityInList);
            } catch (NullPointerException _) { return; }
        }

        userEntityInList.messages.addLast(message);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        CarolSpamUserEntity finalUserEntityInList = userEntityInList;
        scheduler.schedule(() -> {
            finalUserEntityInList.messages.remove(message);
        }, maxSecondsToVerify, TimeUnit.SECONDS);

        scheduler.schedule(scheduler::shutdown, 0, TimeUnit.SECONDS);
    }

    public static boolean isUserSpamming(Member member, int maxMessagesPerSecond)
    {
        CarolSpamUserEntity userEntityInList = null;
        for (CarolSpamUserEntity userEntity : users)
        {
            if (userEntity.id == member.getUser().getIdLong())
            {
                userEntityInList = userEntity;
            }
        }
        if (userEntityInList == null)
        {
            return false;
        }

        Message firstMessage = userEntityInList.messages.getFirst();
        Message lastMessage = userEntityInList.messages.getLast();

        long firstMessageTimestamp = firstMessage.getTimeCreated().toInstant().toEpochMilli();
        long lastMessageTimestamp = lastMessage.getTimeCreated().toInstant().toEpochMilli();

        double messagesPerSecond = (double) userEntityInList.messages.size() / ((double) (lastMessageTimestamp - firstMessageTimestamp) / 1000);

        return messagesPerSecond > maxMessagesPerSecond;
    }

    public static void deleteUserSpammedMessages(Member member)
    {
        CarolSpamUserEntity userEntityInList = null;
        for (CarolSpamUserEntity userEntity : users)
        {
            try {
                if (userEntity.id == member.getIdLong())
                {
                    userEntityInList = userEntity;
                }
            } catch (NullPointerException _) { return; }
        }
        if (userEntityInList == null)
        {
            return;
        }

        for (Message message : userEntityInList.messages)
        {
            try {
                message.delete().queue();
            } catch (Exception _) {}
        }
    }
}
