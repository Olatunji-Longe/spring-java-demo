package com.olonge.demos.domains;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class GeneratedImageMetadata {

    private final String backgroundImagePath;
    private final List<String> foregroundImagePaths;
    private final List<Integer> xCoordinates;
    private final int yCoordinate;
    private final String outputImagePath;
}
