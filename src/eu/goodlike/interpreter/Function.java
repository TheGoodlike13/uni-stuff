package eu.goodlike.interpreter;

import java.util.Collections;
import java.util.Map;

@FunctionalInterface
public interface Function {

    Object execute(Object... args);

    default Map<Object, Object> getMetaData() {
        return Collections.emptyMap();
    }

}
