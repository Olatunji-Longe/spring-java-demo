package com.olonge.demos.controllers;

import com.olonge.demos.domains.GeneratedImageMetadata;
import com.olonge.demos.services.images.ImageGenerationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageGenerationController {

    private final ImageGenerationService imageGenerationService;

    @GetMapping("/generate")
    private ResponseEntity<GeneratedImageMetadata> generateImage() throws Exception {
        GeneratedImageMetadata metadata = imageGenerationService.generateCompositeImage();
        return ResponseEntity.ok(metadata);
    }

    @PostConstruct
    public void trial() throws Exception {
        GeneratedImageMetadata metadata = imageGenerationService.generateCompositeImage();
        System.out.println(metadata);
    }
}
