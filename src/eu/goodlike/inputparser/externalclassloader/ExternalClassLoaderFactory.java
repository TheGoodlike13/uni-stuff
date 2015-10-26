package com.goodlike.inputparser.externalclassloader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a synchronized map of loaders based on directory to avoid re-loading
 */
public class ExternalClassLoaderFactory {

    private static final Map<String, ExternalClassLoader> loaders = Collections.synchronizedMap(new HashMap<>());

    public static synchronized ExternalClassLoader getLoader(String directory) {
        ExternalClassLoader loader = loaders.get(directory);
        if (loader != null)
            return loader;

        try {
            loader = ExternalClassLoader.newInstance(directory);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load directory " + directory);
        }
        loaders.put(directory, loader);

        return loader;
    }

}
