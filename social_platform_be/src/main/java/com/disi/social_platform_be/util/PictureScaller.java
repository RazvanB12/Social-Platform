package com.disi.social_platform_be.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PictureScaller {

    public static byte[] scale(byte[] fileData) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        BufferedImage img = ImageIO.read(in);
        java.awt.Image scaledImage = img.getScaledInstance(256, 256, java.awt.Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(imageBuff, "jpg", buffer);
        return buffer.toByteArray();
    }
}
