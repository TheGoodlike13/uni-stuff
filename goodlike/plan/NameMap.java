package com.goodlike.plan;

import java.util.HashMap;
import java.util.Map;

class NameMap {

    private class AlphabetException extends RuntimeException {
        AlphabetException(String message) {
            super(message);
        }
    }

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MAX_TRAIL = 127;

    private int count;
    private int trail;

    private final Map<String, String> map;

    NameMap() {
        map = new HashMap<>();
        count = 0;
        trail = 1;
    }

    String getKey(String name) {
        return map.keySet().contains(name)
                ?	map.get(name)
                :	getUniqueKey(name);
    }

    private String getUniqueKey(String name) {
        if (count == ALPHABET.length()) {
            count = 0;
            trail++;
        }

        if (trail > MAX_TRAIL)
            throw new AlphabetException("Too many different names for entities. Max allowed: "
                    + MAX_TRAIL * ALPHABET.length());

        String key = ALPHABET.substring(count++, count) + trail;
        map.put(name, key);
        return key;
    }

}
