package com.goodlike.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Permutations<T> {

    private final List<List<T>> allLists;

    private final int[] positions;
    private final List<Integer> finalPositions;

    public Permutations(List<List<T>> allLists) {
        this.allLists = allLists;

        this.positions = new int[allLists.size()];
        this.positions[allLists.size() - 1] = -1;

        Iterator<List<T>> lists = this.allLists.iterator();
        this.finalPositions = Stream.generate(() -> lists.next().size() - 1)
                .limit(allLists.size())
                .collect(Collectors.toList());
    }

    public boolean hasNext() {
        Iterator<Integer> finalPositions = this.finalPositions.iterator();
        return !Arrays.stream(positions).boxed()
                .map(position -> position.equals(finalPositions.next()))
                .reduce(true, (previousPositions, currentPosition) -> previousPositions && currentPosition);
    }

    public List<T> next() {
        int depth = allLists.size() - 1;
        while (depth >= 0) {
            if (finalPositions.get(depth).equals(positions[depth])) {
                positions[depth] = 0;
                depth--;
            }
            else {
                positions[depth]++;
                return makeFromIndexes();
            }
        }
        throw new AssertionError("Failure in permutations.");
    }

    private List<T> makeFromIndexes() {
        Iterator<List<T>> lists = this.allLists.iterator();
        return Arrays.stream(positions).boxed()
                .map(position -> lists.next().get(position))
                .collect(Collectors.toList());
    }

    public List<Integer> indexes() {
        return Arrays.stream(positions).boxed().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return makeFromIndexes().toString();
    }
}
