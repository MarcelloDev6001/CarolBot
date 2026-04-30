package com.marc.discordbot.carol.moderation;

public class CarolModerationAntiLinkManager {
    public static boolean messageContainsLink(String content)
    {
        String contentWithoutSpace = content.replace(" ","");
        return contentWithoutSpace.contains("https://");
    }
}
