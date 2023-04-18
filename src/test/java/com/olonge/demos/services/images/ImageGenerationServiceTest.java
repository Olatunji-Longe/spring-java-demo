package com.olonge.demos.services.images;

import com.olonge.demos.domains.GeneratedImageMetadata;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@MockitoSettings(strictness = Strictness.WARN)
public class ImageGenerationServiceTest {

    private final ImageGenerationService imageGenerationService = new ImageGenerationServiceImpl();


    @BeforeEach
    public void setup() {

    }


//    @Test
    @DisplayName("test generated image")
    public void doTest() throws Exception {
        GeneratedImageMetadata imageMetadata = imageGenerationService.generateCompositeImage();
        assertTrue(imageMetadata.getOutputImagePath().endsWith("output-image.png"));
    }
}
