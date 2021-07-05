package com.kvark900.imageprocessing;

import com.kvark900.imageprocessing.util.ElapsedTimeLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.synchronizedList;
import static java.util.concurrent.Executors.newFixedThreadPool;

@Slf4j
public class ImageResizer {
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    private final BufferedImage[] images;
    private final Integer width;
    private final Integer height;
    private final List<BufferedImage> resizedImages;

    public ImageResizer(BufferedImage[] images, int width, int height) {
        Assert.notNull(images, "Images are null!");
        this.images = images;
        this.width = width;
        this.height = height;
        this.resizedImages = synchronizedList(new ArrayList<>(images.length));
    }

    public List<BufferedImage> resize() {
        new ElapsedTimeLogger<>(ImageResizer.class, this::resizeImages, "Images resizing").log();
        return resizedImages;
    }

    public List<BufferedImage> asyncResize() {
        new ElapsedTimeLogger<>(ImageResizer.class, () ->
                asyncResize(newFixedThreadPool(THREAD_COUNT)), "Async images resizing").log();
        return resizedImages;
    }

    private void resizeImages() {
        for (BufferedImage image : images) {
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = resizeImage(image);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resizedImages.add(bufferedImage);
        }
    }

    private void asyncResize(ExecutorService executorService) {
        try {
            for (BufferedImage image : images)
                executorService.submit(() -> {
                    try {
                        addResizedImage(resizeImage(image));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            shutdownAndAwaitTermination(executorService);
        } catch (Exception e) {
            log.error(e.getMessage());
            shutdownAndAwaitTermination(executorService);
        }
    }

    private void addResizedImage(BufferedImage image) {
        resizedImages.add(image);
        log.info("Thread {} finished task!", Thread.currentThread().getName());
    }

    private BufferedImage resizeImage(BufferedImage bufferedImage) throws InterruptedException {
        log.info("Thread {} started resizing", Thread.currentThread().getName());

        final var outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Image resultingImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        log.info("Thread {} finished resizing", Thread.currentThread().getName());
        return outputImage;
    }

    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Pool did not terminate");
                    System.exit(-1);
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public List<BufferedImage> getResizedImages() {
        return resizedImages;
    }
}
