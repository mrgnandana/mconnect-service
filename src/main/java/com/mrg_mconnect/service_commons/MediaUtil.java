package com.mrg_mconnect.service_commons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

public class MediaUtil {

    public static final String FORMAT_PNG = "PNG";
    public static final String FORMAT_JPG = "JPG";
    public static final int IMAGE_WIDTH = 400;
    public static final int IMAGE_HEIGHT = 400;

    public static String MEDIA_PATH = "";

    public static void createProfileImage(String imageSource, String targetSource) throws Exception {
        try {

            File inputImageFile = new File(imageSource);
            if (!inputImageFile.exists()) {
                throw new Exception("Image does not exists. image source : " + imageSource);
            }

            File graphicThumFile = new File(targetSource);
            graphicThumFile.getParentFile().mkdirs();

            BufferedImage img = ImageIO.read(inputImageFile);

            double imgRatio = (double) img.getHeight() / (double) img.getWidth();

            if (img.getHeight() > img.getWidth()) {
                int resizeHeight = (int) (imgRatio * IMAGE_WIDTH);
                img = resizeImage(img, IMAGE_WIDTH, resizeHeight);
            } else {
                int resizeWidth = (int) (IMAGE_HEIGHT / imgRatio);
                img = resizeImage(img, resizeWidth, IMAGE_HEIGHT);
            }

            BufferedImage cropped = centerCropImage(img, IMAGE_WIDTH, IMAGE_HEIGHT);
            ImageIO.write(cropped, FORMAT_PNG.toLowerCase(), graphicThumFile);

            img.flush();
            cropped.flush();

        } catch (IOException ex) {
            throw new Exception("Image save error");
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

            BufferedImage croppedImage = inputImage.getSubimage((imageWidth / 2) - (width / 2), (imageHeight / 2) - (height / 2), width, height);

            return croppedImage;

        } catch (Exception ex) {
            throw new Exception("Image Crop error");
        }

    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static void fileCopy(String imageSource, String targetSource) throws Exception {
        try {

            File targetFile = new File(targetSource);
            targetFile.getParentFile().mkdirs();

            Files.copy(Paths.get(imageSource), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new Exception("Image copy error");
        }

    }
}
