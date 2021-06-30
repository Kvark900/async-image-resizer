package com.kvark900.imageprocessing;

import org.springframework.util.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageResizer {
    private final BufferedImage[] images;
    private final Integer width;
    private final Integer height;

    public ImageResizer(BufferedImage[] images, int width, int height) {
        Assert.notNull(images, "Images are null!!!");
        this.images = images;
        this.width = width;
        this.height = height;
    }

    public BufferedImage[] resize() {
        return Arrays.stream(images)
                .map(bufferedImage -> {
                    Image resultingImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
                    BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
                    return outputImage;
                }
        ).toArray(BufferedImage[]::new);
    }
}
