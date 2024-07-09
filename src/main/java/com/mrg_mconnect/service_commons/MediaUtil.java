package com.mrg_mconnect.service_commons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MediaUtil {

    public static final String FORMAT_PNG = "PNG";
    public static final String FORMAT_JPG = "JPG";
    public static final int IMAGE_WIDTH = 400;
    public static final int IMAGE_HEIGHT = 400;

    public static String MEDIA_PATH = "";

    public static void createThumbnail(String imageSource, String targetSource) throws Exception {
        try {

            File inputImageFile = new File(imageSource);
            if (!inputImageFile.exists()) {
                throw new Exception("Image does not exists. image source : " + imageSource);
            }

            File graphicThumFile = new File(targetSource);
            graphicThumFile.getParentFile().mkdirs();

            BufferedImage img = ImageIO.read(inputImageFile);
            BufferedImage cropped = centerCropImage(img, IMAGE_WIDTH, IMAGE_HEIGHT);
            ImageIO.write(cropped, FORMAT_PNG.toLowerCase(), graphicThumFile);

            img.flush();
            cropped.flush();

        } catch (IOException ex) {
            throw new Exception("Thumbnail image save error");
        }

    }

    public static BufferedImage centerCropImage(BufferedImage inputImage, int width, int height) throws Exception {
        try {
            int imageWidth = inputImage.getWidth();
            int imageHeight = inputImage.getHeight();

            if (imageWidth < width) {
                width = imageWidth;
            }
            if (imageHeight < height) {
                height = imageHeight;
            }

            BufferedImage croppedImage = inputImage.getSubimage((imageWidth/2)-(width/2),(imageHeight/2)- (height/2), width, height);

            return croppedImage;

        } catch (Exception ex) {
            throw new Exception("Image Crop error");
        }

    }
}
