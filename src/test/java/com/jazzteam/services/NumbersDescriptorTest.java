package com.jazzteam.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersDescriptorTest {
    private NumbersDescriptor numbersDescriptor;

    private static Stream<Arguments> numbersForNumOrder() {
        return Stream.of(
                Arguments.of("123456789", 8),
                Arguments.of("1", 0),
                Arguments.of("10", 1)
        );
    }

    private static Stream<Arguments> numbersForNamedNumOrder() {
        return Stream.of(
                Arguments.of("10", 0, 1),
                Arguments.of("100", 0, 2),
                Arguments.of("1000", 1, 3),
                Arguments.of("10000", 1, 4),
                Arguments.of("100000", 1, 5),
                Arguments.of("1000000", 2, 6)
        );
    }

    @ParameterizedTest
    @DisplayName("Returns valid maximum order of number")
    @MethodSource("numbersForNumOrder")
    void numOrder(String testNumber, int expected) {
        numbersDescriptor = new NumbersDescriptor(testNumber);
        assertEquals(expected, numbersDescriptor.getMaxNumOrder());
    }

    @ParameterizedTest
    @DisplayName("Returns valid named order of number")
    @MethodSource("numbersForNamedNumOrder")
    void namedNumOrder(String testNumber, int expectedNamedOrder, int expectedSimpleOrder) {
        numbersDescriptor = new NumbersDescriptor(testNumber);
        assertEquals(expectedNamedOrder, numbersDescriptor.getNamedNumIndex());
        assertEquals(expectedSimpleOrder, numbersDescriptor.getMaxNumOrder());
    }

}
