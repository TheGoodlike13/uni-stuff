package eu.goodlike.inputparser.externalclassloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

/**
 * Loads .class files from a specified location
 */
public class ExternalClassLoader {

    private final static String DEFAULT_DIRECTORY = ".";

    private final URLClassLoader loader;

    @SuppressWarnings("unused")
    public static ExternalClassLoader newInstance() throws IOException {
        return newInstance(DEFAULT_DIRECTORY);
    }

    public static ExternalClassLoader newInstance(String fileDirectory) throws IOException {
        File file = Paths.get(fileDirectory).toAbsolutePath().toFile();

        if (!file.exists())
            throw new IOException("Directory " + file + " does not exist.");

        if (!file.isDirectory())
            throw new IOException("Directory " + file + " is a file.");

        URL url = file.toURI().toURL();
        System.out.println(url);
        URL[] urls = new URL[]{url};
        return new ExternalClassLoader(new URLClassLoader(urls));
    }

    private ExternalClassLoader(URLClassLoader loader) {
        this.loader = loader;
    }

    public Class<?> loadClass(String className) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read class " + className);
        }
    }

    @SuppressWarnings("unused")
    public void close() throws IOException {
        loader.close();
    }

}
