package eu.goodlike.config;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads the config.xml file and handles all the data within
 * Usually, the following data is stored:
 * a) Web service URLs
 * b) Web service methods and their wrappers
 * c) Directories where the wrappers are contained
 * d) Qualities that can be found in web service method definitions
 */
public class Config {

    private static final String DEFAULT_DIRECTORY = "";
    private static final String CONFIG_FILE = "config.xml";

    private final Map<String, String> services = new HashMap<>();
    private final Map<String, ServiceMethod> methods = new HashMap<>();
    private final Map<String, String> directories = new HashMap<>();
    private final Map<String, String> qualityNames = new HashMap<>();

    public Config() throws ParserConfigurationException, SAXException, IOException {
        this(DEFAULT_DIRECTORY);
    }

    public Config(String directory) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser = parserFactor.newSAXParser();
        ConfigSAXHandler handler = new ConfigSAXHandler();
        InputStream xmlStream = new FileInputStream(Paths.get(directory, CONFIG_FILE).toFile());
        parser.parse(xmlStream, handler);
        handler.getResults(services, methods, directories, qualityNames);
    }

    public String directory(String classID) {
        String directory = directories.get(classID);
        return directory == null ? "" : directory;
    }

    public List<String> method(String methodID) {
        List<String> methodData = methods.get(methodID).toList();
        methodData.set(0, services.get(methodData.get(0)));
        return methodData;
    }

    public Map<String, String> qualityNames() {
        return qualityNames;
    }

    public String getQualityName(String name) {
        return qualityNames.get(name);
    }

}
