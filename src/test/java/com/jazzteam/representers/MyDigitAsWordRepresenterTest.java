package com.jazzteam.representers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.jazzteam.builders.MyDigitAsWordRepresenterBuilder;
import com.jazzteam.builders.MyDigitBuilder;
import com.jazzteam.interfaces.DigitAsWordRepresenter;
import com.jazzteam.models.Grammar;
import com.jazzteam.models.Digit;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyDigitAsWordRepresenterTest {
    static DigitAsWordRepresenter digitAsWordRepresenter;
    private Digit                    digit;

    @BeforeEach
    private void initPrivate() {
        digit = new MyDigitBuilder().getResult();
    }

    @BeforeAll
    static void init() {
        digitAsWordRepresenter = new MyDigitAsWordRepresenterBuilder().getResult();
    }

    private static Stream<Arguments> digitsUnderTwo() {
        return Stream.of(
                Arguments.of("1", 0, "один", "одна"),
                Arguments.of("2", 0, "два", "две")
        );
    }

    private static Stream<Arguments> digitsBetweenThreeAndTen() {
        return Stream.of(
                Arguments.of("0", 0, "ноль", ""),
                Arguments.of("3", 0, "три", ""),
                Arguments.of("4", 0, "четыре", ""),
                Arguments.of("5", 0, "пять", ""),
                Arguments.of("6", 0, "шесть", ""),
                Arguments.of("7", 0, "семь", ""),
                Arguments.of("8", 0, "восемь", ""),
                Arguments.of("9", 0, "девять", "")
        );
    }

    private static Stream<Arguments> numbersBetweenTenAndTwenty() {
        return Stream.of(
                Arguments.of("10", 1, "десять", ""),
                Arguments.of("11", 1, "одиннадцать", ""),
                Arguments.of("13", 1, "тринадцать", ""),
                Arguments.of("14", 1, "четырнадцать", ""),
                Arguments.of("15", 1, "пятнадцать", ""),
                Arguments.of("16", 1, "шестнадцать", ""),
                Arguments.of("17", 1, "семнадцать", ""),
                Arguments.of("18", 1, "восемнадцать", ""),
                Arguments.of("19", 1, "девятнадцать", "")
        );
    }

    private static Stream<Arguments> tens() {
        return Stream.of(
                Arguments.of("2", 1, "двадцать", ""),
                Arguments.of("3", 1, "тридцать", ""),
                Arguments.of("4", 1, "сорок", ""),
                Arguments.of("5", 1, "пятьдесят", ""),
                Arguments.of("6", 1, "шестьдесят", ""),
                Arguments.of("7", 1, "семьдесят", ""),
                Arguments.of("8", 1, "восемьдесят", ""),
                Arguments.of("9", 1, "девяносто", "")
        );
    }

    private static Stream<Arguments> hundreds() {
        return Stream.of(
                Arguments.of("1", 2, "сто", ""),
                Arguments.of("2", 2, "двести", ""),
                Arguments.of("3", 2, "триста", ""),
                Arguments.of("4", 2, "четыреста", ""),
                Arguments.of("5", 2, "пятьсот", ""),
                Arguments.of("6", 2, "шестьсот", ""),
                Arguments.of("7", 2, "семьсот", ""),
                Arguments.of("8", 2, "восемьсот", ""),
                Arguments.of("9", 2, "девятьсот", "")
        );
    }

    @AfterEach
    void reset() {
        digitAsWordRepresenter.reset();
    }

    @ParameterizedTest
    @DisplayName("Builds correct forms of digits, that haven't feminine gender")
    @MethodSource({"digitsBetweenThreeAndTen", "numbersBetweenTenAndTwenty", "tens", "hundreds"})
    void buildsGenderFormsOfDigitsWithoutFeminineForm(String digitSymbol, int positionInTriple,
                                                      String expectedMasculineForm,
                                                      String expectedFeminineForm) {
        digit.setNumber(digitSymbol);
        digit.setPositionInClass(positionInTriple);
        digit.setGenderForm(Grammar.Gender.MALE, expectedMasculineForm);
        digit.setGenderForm(Grammar.Gender.FEMALE, expectedFeminineForm);

        assertEquals(expectedMasculineForm, digitAsWordRepresenter.transcript(digit));
        assertEquals(expectedMasculineForm,
                     digitAsWordRepresenter.withGender(Grammar.Gender.MALE).transcript(digit));
    }

    @ParameterizedTest
    @DisplayName("Builds correct forms of digits with feminine gender")
    @MethodSource("digitsUnderTwo")
    void buildsGenderFormsOfDigitsWithFeminineForm(String digitSymbol, int positionInTriple,
                                                   String expectedMasculineForm,
                                                      String expectedFeminineForm) {
        digit.setNumber(digitSymbol);
        digit.setPositionInClass(positionInTriple);
        digit.setGenderForm(Grammar.Gender.MALE, expectedMasculineForm);
        digit.setGenderForm(Grammar.Gender.FEMALE, expectedFeminineForm);

        assertEquals(expectedMasculineForm, digitAsWordRepresenter.transcript(digit));
        assertEquals(expectedFeminineForm, digitAsWordRepresenter.withGender(Grammar.Gender.FEMALE).transcript(digit));
    }
}
