package com.jazzteam.factories;

import com.jazzteam.models.Digit;
import com.jazzteam.models.Triple;
import com.jazzteam.interfaces.DigitRepository;
import com.jazzteam.interfaces.TripleBuilder;
import com.jazzteam.interfaces.TripleFactory;

public class MyTripleFactory implements TripleFactory {
    private TripleBuilder tripleBuilder;
    private DigitRepository digitRepository;
    private String source;

    @Override
    public TripleBuilder getTripleBuilder() {
        return tripleBuilder;
    }

    @Override
    public void setTripleBuilder(TripleBuilder tripleBuilder) {
        this.tripleBuilder = tripleBuilder;
    }

    @Override
    public Triple create() {
        int sourceLength = source.length();
        assert (sourceLength <= 3);
        assert (tripleBuilder != null);
        tripleBuilder.reset();
        if (!isEmptyNumber()) {
            if (isZeroTriple()) {
                addZeroToTripleBuilder();
            } else {
                for (int i = sourceLength - 1; i > -1; i--) {
                    char currentChar = source.charAt(i);
                    if (isTeens(i, currentChar)) {
                        addTeensToTripleBuilder();
                        break;
                    } else if (!isZeroSymbolAtZeroPosition(i, currentChar)) {
                        addDigitsToBuilderByPositionInTripleAndSymbol(currentChar, i);
                    }
                }
            }
        }
        Triple result = tripleBuilder.getResult();
        return result;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = new StringBuilder(source).reverse().toString();
    }

    @Override
    public DigitRepository getDigitsRepository() {
        return digitRepository;
    }

    @Override
    public void setDigitsRepository(DigitRepository digitRepository) {
        this.digitRepository = digitRepository;
    }

    private void addDigitsToBuilderByPositionInTripleAndSymbol(String symbol, int positionInTriple) {
        Digit digit = digitRepository.getDigitByPositionInTripleAndSymbol(symbol, positionInTriple);
        if (digit != null) tripleBuilder.withDigits(digit);
    }

    private void addDigitsToBuilderByPositionInTripleAndSymbol(char symbol, int positionInTriple) {
        String symbolAsString = String.valueOf(symbol);
        addDigitsToBuilderByPositionInTripleAndSymbol(symbolAsString, positionInTriple);
    }

    private void addZeroToTripleBuilder() {
        addDigitsToBuilderByPositionInTripleAndSymbol("0", 0);
    }


    private void addTeensToTripleBuilder() {
        String symbol = new StringBuilder(source.substring(0, 2)).reverse().toString();
        addDigitsToBuilderByPositionInTripleAndSymbol(symbol, 1);
    }

    private boolean isEmptyNumber() {
        boolean result = source.isEmpty();
        return result;
    }

    private boolean isZeroTriple() {
        boolean result = source.equals("0");
        result |= source.equals("00");
        result |= source.equals("000");
        return result;
    }

    private boolean isTeens(int pos, char symbol) {
        int sourceLength = source.length();
        boolean result = symbol == '1';
        result &= sourceLength > 1;
        result &= pos == 1;
        return result;
    }

    private boolean hasTeens() {
        boolean result = source.length() > 1 && isTeens(1, source.charAt(1));
        return result;
    }

    private boolean isZeroSymbolAtZeroPosition(int i, char currentChar) {
        return currentChar == '0' && i == 0;
    }

}
