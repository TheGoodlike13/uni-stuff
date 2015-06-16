package com.goodlike.utils.permutations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Permutations<T> implements Iterable<List<T>> {

    @Override
    public Iterator<List<T>> iterator() {
        return lists == null || lists.isEmpty() ? new EmptyPermutator<>() : new Permutator();
    }

    // CONSTRUCTORS

    public Permutations(List<List<T>> lists) {
        this.lists = lists;
    }

    // PRIVATE

    private final List<List<T>> lists;

    private class Permutator implements Iterator<List<T>> {

        @Override
        public boolean hasNext() {
            return index >= 0;
        }

        @Override
        public List<T> next() {
            List<T> list = new ArrayList<>();
            for (int i = 0; i < currentIndexes.length; i++)
                list.add(lists.get(i).get(currentIndexes[i]));

            adjustIndexes();

            return list;
        }

        // CONSTRUCTORS

        private Permutator() {
            currentIndexes = new int[lists.size()];
            index = lists.size() - 1;
        }

        // PRIVATE

        private final int[] currentIndexes;
        private int index;

        private void adjustIndexes() {
            currentIndexes[index]++;
            while (currentIndexes[index] >= lists.get(index).size()) {
                currentIndexes[index] = 0;
                if (--index < 0)
                    return;

                currentIndexes[index]++;
            }

            index = lists.size() - 1;
        }

    }

    private static class EmptyPermutator<T> implements Iterator<List<T>> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public List<T> next() {
            return Collections.emptyList();
        }

    }

}
