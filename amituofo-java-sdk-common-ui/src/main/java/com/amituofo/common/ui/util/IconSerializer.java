package com.amituofo.common.ui.util;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class IconSerializer {

    /**
     * 将 Icon 转为 PNG 格式的 byte[]
     */
    public static byte[] iconToBytes(Icon icon) throws IOException {
        if (icon == null) return null;

        // 创建 BufferedImage
        BufferedImage image = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        // 绘制 Icon 到 Image
        Graphics2D g2d = image.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        // 写入 PNG 流
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        }
    }

    /**
     * 从 PNG byte[] 重建 ImageIcon
     */
    public static ImageIcon bytesToIcon(byte[] data) throws IOException {
        if (data == null || data.length == 0) return null;
        try (InputStream in = new ByteArrayInputStream(data)) {
            BufferedImage image = ImageIO.read(in);
            return new ImageIcon(image);
        }
    }
}