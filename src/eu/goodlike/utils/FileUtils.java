package eu.goodlike.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class FileUtils {

    //No instantiation
    private FileUtils() {throw new AssertionError();}

    //Returns all lines from a file, throws exception if it is not possible
    public static List<String> allLines(String file) {
        Path path = Paths.get(file).toAbsolutePath();
        try {
            return Files.readAllLines(path, UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read from file " + path);
        }
    }

}
