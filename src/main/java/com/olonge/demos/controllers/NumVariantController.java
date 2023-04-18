package com.olonge.demos.controllers;

import com.olonge.demos.services.images.ImageGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/num")
public class NumVariantController {

    private final ImageGenerationService imageGenerationService;

    @GetMapping("/variant")
    private ResponseEntity<String> generateImage() throws Exception {
        imageGenerationService.generateCompositeImage();
        return ResponseEntity.ok("OK");
    }
}
