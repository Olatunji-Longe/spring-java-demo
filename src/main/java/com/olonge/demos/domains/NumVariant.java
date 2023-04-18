package com.olonge.demos.domains;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum NumVariant {
    NONE(Num.builder().build()),
    A(Num.builder().type("A").digit(1).build()),
    B(Num.builder().type("B").digit(2).build()),
    C(Num.builder().type("C").digit(3).build());

    private final Num num;

    NumVariant(Num num) {
        this.num = num;
    }

    public NumVariant of(int value) {
        return Stream.of(values())
                .filter(variant -> variant.num.getDigit() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("message"));
    }

}
