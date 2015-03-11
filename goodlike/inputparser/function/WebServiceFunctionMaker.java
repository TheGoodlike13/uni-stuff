package com.goodlike.inputparser.function;

import com.goodlike.config.Config;
import com.goodlike.inputparser.SOAP.SOAPMessager;
import com.goodlike.inputparser.SOAP.WSRequest;
import com.goodlike.inputparser.SOAP.WSResponse;
import com.goodlike.inputparser.externalclassloader.ExternalClassLoader;
import com.goodlike.inputparser.externalclassloader.ExternalClassLoaderFactory;
import com.goodlike.inputparser.wsdl.WSDLParser;
import com.goodlike.inputparser.wsdl.WSDLParserFactory;
import com.goodlike.interpretator.Function;
import com.goodlike.utils.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebServiceFunctionMaker implements FunctionMaker {

    @Override
    public List<Function> makeFunctions(List<String> definitions, Config config) {
        List<Function> functions = new ArrayList<>();
        for (int i = 0; i < definitions.size() / 5; i++)
            functions.add(makeFunction(definitions.subList(5 * i, 5 * (i + 1)), config));
        return functions;
    }

    private Function makeFunction(List<String> definitions, Config config) {
        String url = definitions.get(0);
        String operation = definitions.get(1);
        String port = definitions.get(2);
        String requestName = definitions.get(3);
        String responseName = definitions.get(4);

        List<String> qualityNames = config.qualityNames();

        WSDLParser parser = WSDLParserFactory.getParser();
        Map<String, String> wsData = parser.parse(url, operation, port, qualityNames);

        WSRequest wsRequest = request(config, requestName);
        WSResponse wsResponse = response(config, responseName);

        Map<String, Double> qualities = qualityNames.stream()
                .collect(Collectors.toMap(java.util.function.Function.<String>identity(),
                        quality -> Double.valueOf(wsData.get(quality))));

        return SOAPMessager.newInstance(wsData, port, qualities, wsRequest, wsResponse);
    }

    private WSRequest request(Config config, String requestName) {
        String directory = config.directory(requestName);
        ExternalClassLoader loader = ExternalClassLoaderFactory.getLoader(directory);
        Class<?> requestClass = loader.loadClass(requestName);
        Object requestObject = ClassUtils.instantiate(requestClass);
        Method requestExecution = ClassUtils.getMethod(requestClass, "WSRequest", "objectsToRequest", Object[].class);
        return objects -> {
            try {
                return (String) requestExecution.invoke(requestObject, new Object[] {objects});
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to transform request " + requestName);
            }
        };
    }

    private WSResponse response(Config config, String responseName) {
        String directory = config.directory(responseName);
        ExternalClassLoader loader = ExternalClassLoaderFactory.getLoader(directory);
        Class<?> responseClass = loader.loadClass(responseName);
        Object responseObject = ClassUtils.instantiate(responseClass);
        Method responseExecution = ClassUtils.getMethod(responseClass, "WSResponse", "responseToObject", String.class);
        return response -> {
            try {
                return responseExecution.invoke(responseObject, response);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to transform response " + responseName);
            }
        };
    }
}
