package com.goodlike.inputparser;

import com.goodlike.chaining.ChainingType;
import com.goodlike.config.Config;
import com.goodlike.inputparser.function.Modifier;
import com.goodlike.inputparser.quality.Quality;
import com.goodlike.inputparser.quality.QualityMode;
import com.goodlike.inputparser.quality.QualityType;
import com.goodlike.interpreter.Function;
import com.goodlike.interpreter.RuleData;
import com.goodlike.utils.CollectionUtils;
import com.goodlike.utils.FileUtils;
import com.goodlike.utils.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation for input file parsing
 */
public class DefaultParser implements Parser {

    private final Iterator<Integer> ruleNumbers;

    public DefaultParser() {
        ruleNumbers = Stream.iterate(1, n -> n + 1).iterator();
    }

    @Override
    public InputData parse(String file, Config config) {
        List<String> allLines = FileUtils.allLines(file);
        allLines.add("");

        List<RuleData> ruleData = CollectionUtils.getPart(allLines, "Rules", "").stream()
                .map(stringToRule(config))
                .collect(Collectors.toList());

        List<String> factStrings = StringUtils.spaceSplit(CollectionUtils.getFirst(allLines, "Facts", ""));
        String goalString = CollectionUtils.getFirst(allLines, "Goal", "");
        ChainingType method = ChainingType.getMethod(CollectionUtils.getFirst(allLines, "Method", ""));

        List<Quality> qualityList = CollectionUtils.getPart(allLines, "Qualities", "").stream()
                .map(stringToQuality(config))
                .collect(Collectors.toList());

        return new InputData(ruleData, factStrings, goalString, method, qualityList);
    }

    private java.util.function.Function<String, Quality> stringToQuality(Config config) {
        return line -> {
            List<String> qualityDefinitions = StringUtils.spaceSplit(line);

            String simpleName = qualityDefinitions.get(0);
            String name = config.getQualityName(simpleName);
            QualityType qualityType = QualityType.getType(qualityDefinitions.get(1));
            QualityMode qualityMode = QualityMode.getMode(qualityDefinitions.get(2));
            double qualityValue = Double.valueOf(qualityDefinitions.get(3));
            Quality quality = new Quality(name, qualityType, qualityMode, qualityValue);
            if (qualityDefinitions.size() > 4) {
                double coefficient = Double.valueOf(qualityDefinitions.get(4));
                quality.setCoefficient(coefficient);
            }
            return quality;
        };
    }

    private java.util.function.Function<String, RuleData> stringToRule(Config config) {
        return fullLine -> {
            int index = fullLine.indexOf("  ");
            String ruleDescriptor = fullLine.substring(0, index);

            String line = fullLine.substring(index);
            List<String> definitions = StringUtils.spaceSplit(line);

            String modifier = definitions.remove(0);
            List<Function> functions = Modifier.getInstance(modifier).makeFunctions(definitions, config);
            Function[] functionDescriptors = functions.toArray(new Function[functions.size()]);

            return new RuleData(ruleDescriptor, ruleNumbers.next(), functionDescriptors);
        };
    }

}
