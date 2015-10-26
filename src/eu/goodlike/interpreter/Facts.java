package eu.goodlike.interpreter;

import java.util.HashMap;
import java.util.Map;

class Facts {

    private Map<String, Object> map;

    Facts() {
        map = new HashMap<>();
    }

    Object get(String name) {
        return map.get(name);
    }

    void put(String name, Object value) {
        if (map.get(name) != null)
            throw new IllegalStateException("Attempted to overwrite " + name);

        if (value == null)
            throw new NullPointerException("Attempted to add a null value to " + name);

        map.put(name, value);
    }

    int getSize() {
        return map.size();
    }

}
