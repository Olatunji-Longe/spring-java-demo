package com.olonge.demos.domains;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class Num {
    private final String type;
    private final int digit;

}
