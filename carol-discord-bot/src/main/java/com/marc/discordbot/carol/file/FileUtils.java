package com.marc.discordbot.carol.file;

import java.io.File;

public class FileUtils {
    public static File getFileFromResourceFolder(String filePath)
    {
        return new File("/resources/" + filePath);
    }
}
