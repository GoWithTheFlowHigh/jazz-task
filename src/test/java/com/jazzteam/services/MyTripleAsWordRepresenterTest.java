package com.jazzteam.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.jazzteam.builders.MyTripleFactoryBuilder;
import com.jazzteam.factories.MyNamedClassFactoryRepository;
import com.jazzteam.interfaces.NamedClassRepository;
import com.jazzteam.interfaces.TripleAsWordRepresenter;
import com.jazzteam.builders.MyTripleAsWordRepresenterBuilder;
import com.jazzteam.interfaces.TripleFactory;
import com.jazzteam.models.Triple;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTripleAsWordRepresenterTest {
    static private TripleAsWordRepresenter tripleAsWordRepresenter;
    static private TripleFactory             tripleFactory;
    static private NamedClassRepository namedClassRepository;

    @BeforeAll
    static void init() {
        namedClassRepository = new MyNamedClassFactoryRepository().create();
        initTripleAsWordsRepresenter();
        initTripleFactory();
    }

    static void initTripleAsWordsRepresenter() {
        tripleAsWordRepresenter = new MyTripleAsWordRepresenterBuilder().getResult();
    }

    static void initTripleFactory() {
        tripleFactory = new MyTripleFactoryBuilder().getResult();
    }

    static Stream<Arguments> triples() {
        return Stream.of(
                Arguments.of("0", 0, "ноль"),
                Arguments.of("00", 0, "ноль"),
                Arguments.of("000", 0, "ноль"),
                Arguments.of("0", 1, ""),
                Arguments.of("00", 1, ""),
                Arguments.of("000", 1, ""),
                Arguments.of("1", 0, "один"),
                Arguments.of("01", 0, "один"),
                Arguments.of("001", 0, "один"),
                Arguments.of("12", 0, "двенадцать"),
                Arguments.of("012", 0, "двенадцать"),
                Arguments.of("123", 0, "сто двадцать три"),
                Arguments.of("122", 0, "сто двадцать два"),
                Arguments.of("123", 1, "сто двадцать три тысячи"),
                Arguments.of("122", 1, "сто двадцать две тысячи"),
                Arguments.of("121", 1, "сто двадцать одна тысяча"),
                Arguments.of("1", 1, "одна тысяча"),
                Arguments.of("01", 1, "одна тысяча"),
                Arguments.of("001", 1, "одна тысяча"),
                Arguments.of("2", 1, "две тысячи"),
                Arguments.of("02", 1, "две тысячи"),
                Arguments.of("002", 1, "две тысячи"),
                Arguments.of("5", 1, "пять тысяч"),
                Arguments.of("05", 1, "пять тысяч"),
                Arguments.of("005", 1, "пять тысяч"),
                Arguments.of("11", 1, "одиннадцать тысяч"),
                Arguments.of("011", 1, "одиннадцать тысяч"),
                Arguments.of("122", 1, "сто двадцать две тысячи"),
                Arguments.of("123", 2, "сто двадцать три миллиона"),
                Arguments.of("121", 2, "сто двадцать один миллион"),
                Arguments.of("12", 2, "двенадцать миллионов"),
                Arguments.of("012", 2, "двенадцать миллионов"),
                Arguments.of("1", 2, "один миллион"),
                Arguments.of("01", 2, "один миллион"),
                Arguments.of("001", 2, "один миллион"),
                Arguments.of("2", 2, "два миллиона"),
                Arguments.of("02", 2, "два миллиона"),
                Arguments.of("002", 2, "два миллиона"),
                Arguments.of("20", 2, "двадцать миллионов"),
                Arguments.of("020", 2, "двадцать миллионов"),
                Arguments.of("123", 2, "сто двадцать три миллиона"),
                Arguments.of("12", 2, "двенадцать миллионов"),
                Arguments.of("012", 2, "двенадцать миллионов")
        );
    }

    @ParameterizedTest
    @DisplayName("Generates correct string representation of triple")
    @MethodSource("triples")
    void stringRepresentationOfTriple(String tripleSource, int orderNumber,
                                      String expectedStringRepresentationOfTriple) {
        tripleFactory.setSource(tripleSource);
        Triple actualTriple = tripleFactory.create();
        actualTriple.setNamedClass(namedClassRepository.getByNumber(orderNumber));
        assertEquals(expectedStringRepresentationOfTriple, tripleAsWordRepresenter.transcript(actualTriple));
    }
}
