package com.goodlike.inputparser;

import com.goodlike.chaining.ChainingType;
import com.goodlike.config.Config;
import com.goodlike.inputparser.function.Modifier;
import com.goodlike.inputparser.quality.Quality;
import com.goodlike.inputparser.quality.QualityMode;
import com.goodlike.inputparser.quality.QualityType;
import com.goodlike.interpretator.Function;
import com.goodlike.interpretator.RuleData;
import com.goodlike.utils.CollectionUtils;
import com.goodlike.utils.FileUtils;
import com.goodlike.utils.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultParser implements Parser {

    private final Iterator<Integer> ruleNumbers;

    public DefaultParser() {
        ruleNumbers = Stream.iterate(1, n -> n + 1).iterator();
    }

    @Override
    public InputData parse(String file, Config config) {
        List<String> allLines = FileUtils.allLines(file);

        List<RuleData> ruleData = CollectionUtils.getPart(allLines, "Rules", "").stream()
                .map(stringToRule(config))
                .collect(Collectors.toList());

        List<String> factStrings = StringUtils.spaceSplit(CollectionUtils.getFirst(allLines, "Facts", ""));
        String goalString = CollectionUtils.getFirst(allLines, "Goal", "");
        ChainingType method = ChainingType.getMethod(CollectionUtils.getFirst(allLines, "Goal", ""));

        List<Quality> qualityList = CollectionUtils.getPart(allLines, "Qualities", "").stream()
                .map(stringToQuality())
                .collect(Collectors.toList());

        return new InputData(ruleData, factStrings, goalString, method, qualityList);
    }

    private java.util.function.Function<String, Quality> stringToQuality() {
        return line -> {
            List<String> qualityDefinitions = StringUtils.spaceSplit(line);

            String name = qualityDefinitions.get(0);
            QualityType qualityType = QualityType.getType(qualityDefinitions.get(1));
            QualityMode qualityMode = QualityMode.getMode(qualityDefinitions.get(2));
            double qualityValue = Double.valueOf(qualityDefinitions.get(3));

            return new Quality(name, qualityType, qualityMode, qualityValue);
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
