package com.olonge.demos.services.images;

import com.olonge.demos.domains.GeneratedImageMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ImageGenerationServiceImpl implements ImageGenerationService {

    private static final int BG_TILE_WIDTH = 200;
    private static final int BG_TILE_HEIGHT = 120;
    private static final int FOREGROUND_IMAGE_SPACING = 6;
    private static final int FOREGROUND_IMAGE_WIDTH = 34;
    private static final int FOREGROUND_IMAGE_HEIGHT = 34;
    private static final String OUTPUT_IMG_DIR = "./generated-images";

    @Override
    public GeneratedImageMetadata generateCompositeImage() throws Exception {
        final BufferedImage backgroundImage = getBackgroundImage();
        final List<BufferedImage> foregroundImages = getForegroundImages();
        final List<Integer> xCoordinates = getXCoordinates(foregroundImages.size());
        final int yCoordinate = getYCoordinate();

        return new ImageGeneratorTask(
                backgroundImage,
                foregroundImages,
                xCoordinates,
                yCoordinate
        ).call();
    }

    private BufferedImage getBackgroundImage() {
        return new BufferedImage(
                BG_TILE_WIDTH,
                BG_TILE_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
    }

    private List<BufferedImage> getForegroundImages() throws MalformedURLException {
        List<URL> foregroundImageURls = getForegroundImageURls();
        List<BufferedImage> foregroundImages = new ArrayList<>();
        foregroundImageURls.forEach(imgUrl ->
                readImageFromUrl(imgUrl).ifPresent(foregroundImages::add)
        );
        return foregroundImages;
    }

    private List<Integer> getXCoordinates(int numOfImages) {
        List<Integer> xCoordinates = new ArrayList<>(numOfImages + 1);
        xCoordinates.add(FOREGROUND_IMAGE_SPACING);
        for (int n = 0; n < numOfImages; n++) {
            xCoordinates.add(xCoordinates.get(n) + FOREGROUND_IMAGE_WIDTH - (FOREGROUND_IMAGE_SPACING / 2));
        }
        return xCoordinates;
    }

    private static int getYCoordinate() {
        return BG_TILE_HEIGHT - (FOREGROUND_IMAGE_SPACING + FOREGROUND_IMAGE_HEIGHT);
    }

    private List<URL> getForegroundImageURls() throws MalformedURLException {
        return List.of(
                new URL("http://www.java2s.com/style/download.png"),
                new URL("http://www.java2s.com/style/download.png"),
                new URL("http://www.java2s.com/style/download.png")
        );
    }

    private Optional<BufferedImage> readImageFromUrl(URL imageUrl) {
        try {
            return Optional.of(ImageIO.read(imageUrl));
        } catch (IOException ex) {
            log.error("Exception occurred: {}", ex.getMessage());
        }
        return Optional.empty();
    }

    private static class ImageGeneratorTask implements Callable<GeneratedImageMetadata> {

        private final BufferedImage backgroundImage;
        private final List<BufferedImage> foregroundImages;
        private final List<Integer> xCoordinates;
        private final int yCoordinate;
        private final BufferedImage outputImage;

        public ImageGeneratorTask(BufferedImage backgroundImage, List<BufferedImage> foregroundImages, List<Integer> xCoordinates, int yCoordinate) {
            this.backgroundImage = backgroundImage;
            this.foregroundImages = foregroundImages;
            this.xCoordinates = xCoordinates;
            this.yCoordinate = yCoordinate;
            this.outputImage = getOutputImage(backgroundImage);
        }

        @Override
        public GeneratedImageMetadata call() throws Exception {
            final GeneratedImageMetadata.GeneratedImageMetadataBuilder metadataBuilder =
                    GeneratedImageMetadata.builder()
                            .xCoordinates(xCoordinates)
                            .yCoordinate(yCoordinate);

            for (int n = 0; n < foregroundImages.size(); n++) {
                outputImage.getGraphics().drawImage(
                        foregroundImages.get(n).getScaledInstance(
                                FOREGROUND_IMAGE_WIDTH,
                                FOREGROUND_IMAGE_HEIGHT,
                                Image.SCALE_SMOOTH
                        ),
                        xCoordinates.get(n),
                        yCoordinate,
                        null
                );
            }

            File directory = new File(OUTPUT_IMG_DIR);
            boolean dirExists = directory.exists() || directory.mkdirs();
            if (dirExists) {
                writeImageToPng(backgroundImage, "bg-image.png")
                        .ifPresent(metadataBuilder::backgroundImagePath);

                AtomicInteger n = new AtomicInteger(0);
                final List<String> fgImagePaths = new ArrayList<>();
                foregroundImages.forEach(foregroundImage -> {
                    writeImageToPng(foregroundImage, "fg-image-" + n.incrementAndGet() + ".png")
                            .ifPresent(fgImagePaths::add);
                });
                metadataBuilder.foregroundImagePaths(fgImagePaths);

                writeImageToPng(outputImage, "output-image.png")
                        .ifPresent(metadataBuilder::outputImagePath);
            } else {
                throw new IllegalStateException(String.format("Directory %s does not exist", directory));
            }
            return metadataBuilder.build();
        }

        private BufferedImage getOutputImage(BufferedImage backgroundImage) {
            return new BufferedImage(
                    backgroundImage.getWidth(),
                    backgroundImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
        }

        private Optional<String> writeImageToPng(BufferedImage image, String imageName) {
            try {
                File file = new File(OUTPUT_IMG_DIR + File.separator + imageName);
                ImageIO.write(image, "png", file);
                return Optional.of(file.getPath());
            } catch (IOException ex) {
                log.error("Exception occurred: {}", ex.getMessage());
            }
            return Optional.empty();
        }
    }
}
