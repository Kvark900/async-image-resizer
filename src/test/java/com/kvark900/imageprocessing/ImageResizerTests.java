package com.kvark900.imageprocessing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ImageResizerTests {

    private ImageResizer imageResizer;
    private static final int IMAGE_COUNT = 100;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;

    @Test
    public void throwExceptionWhenImagesAreNull() {
        assertThrows(IllegalArgumentException.class,
                () -> imageResizer = new ImageResizer(null, WIDTH,HEIGHT)
        );
    }

    @Test
    @DisplayName("Resized images count equals original images count")
    public void correctCountOfResizedImages() throws IOException {
        ImageResizer imageResizer = new ImageResizer(getTestImages(), WIDTH, HEIGHT);
        BufferedImage[] images = imageResizer.resize();
        assertNotNull(images);
        assertEquals(images.length, IMAGE_COUNT);
        assertFalse(incorrectTypeAfterResizing(images));
    }

    @Test
    public void imagesSuccessfullyInitialized() {
        assertDoesNotThrow(() -> {
            BufferedImage[] testImages = getTestImages();
            assertTrue(testImages.length == IMAGE_COUNT && !hasNullElement(testImages));
        });
    }

    @Test
    public void imageSuccessFullyDiscovered() throws IOException {
        long size = Files.size(Paths.get("C:\\Users\\Zigovic\\Documents\\img-test\\1.jpg"));
        assertTrue(size != 0);
    }

    public boolean incorrectTypeAfterResizing(BufferedImage[] imgs) {
        return Arrays.stream(imgs).anyMatch(bufferedImage -> !bufferedImage.getClass().equals(BufferedImage.class));
    }

    private boolean hasNullElement(BufferedImage[] imgs)
    {
        return Arrays.stream(imgs).anyMatch((Objects::isNull));
    }

    private  BufferedImage[] getTestImages() throws IOException {
        BufferedImage[] imgs = new BufferedImage[IMAGE_COUNT];
        BufferedImage in = ImageIO.read(getImage());
        Arrays.fill(imgs, in);
        return imgs;
    }

    private File getImage() {
        return new File("C:\\Users\\Zigovic\\Documents\\img-test\\1.jpg");
    }


}
