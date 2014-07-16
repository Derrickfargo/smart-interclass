/*
 * Pic.java
 *
 * Created on 2007年11月19日, 下午4:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package cn.com.incito.server.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tewang
 */
public class Pic {

    /** Creates a new instance of Pic */
    public Pic() {
    }

    /**
     * Returns an ImageIcon,or null if the path waw invalid.
     */
    public static ImageIcon crateIcon(String filePath) {
        URL url = Pic.class.getResource(filePath);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            System.err.println("Couldn't find file:" + filePath);
            return null;
        }
    }

    /**
     * 把图片切成一张一张的
     * @param image
     * @param width 每一张图片的宽度
     * @param height第一张图片的高度
     * @return
     */
    public static BufferedImage[] getBufferedImages(BufferedImage image, int subWidth, int subHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        int columnCount = width / subWidth;
        int rowCount = height / subHeight;
        BufferedImage[] buf = new BufferedImage[rowCount * columnCount];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < columnCount; j++) {
                int k = columnCount * i + j;
                buf[k] = image.getSubimage(j * subWidth, i * subHeight, subWidth, subHeight);
            }
        }
        return buf;
    }

    /**
     * 对图片进行透明度处理
     * @param buf
     * @return
     */
    public static BufferedImage doLucency(BufferedImage buf, Color c) {
        int crgb = c.getRGB();
        System.out.println("crgb:" + crgb);
        int width = buf.getWidth();
        int height = buf.getHeight();
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                int rgb = buf.getRGB(j, k);
                System.out.println("rgb:" + rgb);
                if ((crgb ^ rgb) == 0) {
                    System.out.println("same");
                    temp.setRGB(j, k, 0x00FFFFFF);
                } else {
                    temp.setRGB(j, k, rgb);
                }
            }
        }
        buf = temp;
        return buf;
    }

    /**
     * 对图片进行透明度处理
     * @param buf
     * @return
     */
    public static BufferedImage[] doLucency(BufferedImage[] buf, Color c) {
        for (int i = 0; i < buf.length; i++) {
            buf[i] = doLucency(buf[i], c);
        }
        return buf;
    }

    /**
     * 保存图片 
     * @param buf
     * @param file保存文件夹
     * @return
     */
    public static boolean saveBufferedImage(BufferedImage[] buf, File file) {
        for (int i = 0; i < buf.length; i++) {
            FileOutputStream fous = null;
            try {
                fous = new FileOutputStream(new File(file, i + ".png"));
                ImageIO.write(buf[i], "png", fous);
            } catch (IOException ex) {
                Logger.getLogger(Pic.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fous.close();
                } catch (IOException ex) {
                    Logger.getLogger(Pic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
}
