package com.jazzteam.factories;

import com.jazzteam.models.Triple;
import com.jazzteam.models.TripleSequence;
import com.jazzteam.interfaces.NamedClassRepository;
import com.jazzteam.interfaces.TripleFactory;
import com.jazzteam.interfaces.TripleSequenceBuilder;
import com.jazzteam.interfaces.TripleSequenceFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyTripleSequenceFactory implements TripleSequenceFactory {
    private NamedClassRepository namedClassRepository;
    private String source;
    private TripleFactory tripleFactory;
    private TripleSequenceBuilder tripleSequenceBuilder;

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public TripleSequence create() {
        assert (!source.isEmpty());
        assert (tripleSequenceBuilder != null);
        assert (tripleFactory != null);
        assert (namedClassRepository != null);
        tripleSequenceBuilder.reset();
        createTriplesFromSource();
        TripleSequence result = tripleSequenceBuilder.getResult();
        return result;
    }

    @Override
    public TripleFactory getTripleFactory() {
        return tripleFactory;
    }

    @Override
    public void setTripleFactory(TripleFactory tripleFactory) {
        this.tripleFactory = tripleFactory;
    }

    @Override
    public NamedClassRepository getNamedClassRepository() {
        return namedClassRepository;
    }

    @Override
    public void setNamedClassRepository(NamedClassRepository namedClassRepository) {
        this.namedClassRepository = namedClassRepository;
    }

    @Override
    public TripleSequenceBuilder getTripleSequenceBuilder() {
        return tripleSequenceBuilder;
    }

    @Override
    public void setTripleSequenceBuilder(TripleSequenceBuilder tripleSequenceBuilder) {
        this.tripleSequenceBuilder = tripleSequenceBuilder;
    }

    private List<String> sliceSourceIntoTriples() {
        List<String> result = sliceStringBy(source, 3);
        return result;
    }

    private List<String> sliceStringBy(String string, int size) {
        List<String> result = new LinkedList<>();
        int sourceLength = string.length();
        int startOfRange = 0;
        int endOfRange = sourceLength / size;
        if (endOfRange * size < sourceLength) endOfRange += 1;
        int difference = sourceLength % size;
        int offset = size - difference;
        IntStream.range(startOfRange, endOfRange).forEach(pos -> {
            int start = pos * size;
            if (difference > 0 && pos > 0) start -= offset;
            int end = start + size;
            if (difference > 0 && pos == 0) end -= offset;
            if (end > sourceLength) end = sourceLength;
            result.add(string.substring(start, end));
        });
        return result;
    }

    private void createTriplesFromSource() {
        List<Triple> result = sliceSourceIntoTriples().stream().map(s -> {
            tripleFactory.setSource(s);
            return tripleFactory.create();
        }).collect(Collectors.toList());
        int resultSize = result.size();
        IntStream.range(0, resultSize)
                .forEach(pos -> {
                    int orderNumber = resultSize - pos - 1;
                    result.get(pos).setNamedClass(namedClassRepository.getByNumber(orderNumber));
                });
        if (!result.stream().allMatch(triple -> triple.isZero())) {
            result.removeIf(triple -> triple.isZero() && resultSize > 1);
        }
        tripleSequenceBuilder.withTriples(result);
    }
}
