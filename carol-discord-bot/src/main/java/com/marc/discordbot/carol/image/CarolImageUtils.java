package com.marc.discordbot.carol.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class CarolImageUtils {
    public static BufferedImage getImageFromURL(String url)
    {
        try {
            return ImageIO.read(new URL(url));
        } catch (Exception e) {
            return null;
        }
    }

    public static ByteArrayInputStream toByteArray(BufferedImage bufferedImage)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os); // You can choose "jpg", "gif", etc.
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new ByteArrayInputStream(os.toByteArray());
    }
}
