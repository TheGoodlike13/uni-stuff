package eu.goodlike.inputparser.function;

import eu.goodlike.config.Config;
import eu.goodlike.inputparser.externalclassloader.ExternalClassLoader;
import eu.goodlike.inputparser.externalclassloader.ExternalClassLoaderFactory;
import eu.goodlike.interpreter.Function;
import eu.goodlike.utils.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Returns a list of functions which use .class files for their implementation
 */
public class FileFunctionMaker implements FunctionMaker {

    @Override
    public List<Function> makeFunctions(List<String> definitions, Config config) {
        return definitions.stream()
                .map(className -> {
                    String directory = config.directory(className);
                    ExternalClassLoader loader = ExternalClassLoaderFactory.getLoader(directory);
                    Class<?> function = loader.loadClass(className);
                    Object functionObject = ClassUtils.instantiate(function);
                    Method execution = ClassUtils.getMethod(function, "Function", "execute", Object[].class);

                    return (Function) args -> {
                        try {
                            return execution.invoke(functionObject, new Object[] {args});
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException("Failed to execute function.");
                        }
                    };
                })
                .collect(Collectors.toList());
    }

}
