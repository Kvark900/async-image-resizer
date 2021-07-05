package com.kvark900.imageprocessing;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ImageResizerTests {
    private static final int IMAGE_COUNT = 2;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    public static final String TEST_IMAGE_PATH = "src/main/resources/static/img-test/1.jpg";

    @Test
    public void throwExceptionWhenImagesAreNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageResizer(null, WIDTH,HEIGHT)
        );
    }

    @Test
    public void imagesInitialized() {
        assertDoesNotThrow(() -> {
            BufferedImage[] testImages = getTestImages();
            assertTrue(testImages.length == IMAGE_COUNT && !hasNullElement(testImages));
        });
    }

    @Test
    public void imageDiscovered() throws IOException {
        long size = Files.size(Paths.get(TEST_IMAGE_PATH));
        assertTrue(size != 0);
    }

    @Test
    public void resize() throws IOException {
        ImageResizer imageResizer = new ImageResizer(getTestImages(), WIDTH, HEIGHT);
        List<BufferedImage> images = imageResizer.resize();
        assertNotNull(images);
        assertEquals(images.size(), IMAGE_COUNT);
        assertFalse(incorrectTypeAfterResizing(images));
    }

    @Test
    public void asyncResize() throws IOException {
        ImageResizer imageResizer = new ImageResizer(getTestImages(), WIDTH, HEIGHT);
        List<BufferedImage> images = imageResizer.asyncResize();
        assertNotNull(images);
        assertEquals(images.size(), IMAGE_COUNT);
        assertFalse(incorrectTypeAfterResizing(images));
    }

    public boolean incorrectTypeAfterResizing(List<BufferedImage> imgs) {
        return imgs.stream().anyMatch(bufferedImage -> !bufferedImage.getClass().equals(BufferedImage.class));
    }

    private boolean hasNullElement(BufferedImage[] imgs) {
        return Arrays.stream(imgs).anyMatch((Objects::isNull));
    }

    private  BufferedImage[] getTestImages() throws IOException {
        BufferedImage[] imgs = new BufferedImage[IMAGE_COUNT];
        BufferedImage in = ImageIO.read(getImage());
        Arrays.fill(imgs, in);
        return imgs;
    }

    private File getImage() {
        return new File(TEST_IMAGE_PATH);
    }
}
