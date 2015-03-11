package com.goodlike.utils;

import java.lang.reflect.Method;

public class ClassUtils {

    public static Object instantiate(Class<?> classObject) {
        try {
            return classObject.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to instantiate class " + classObject);
        }
    }

    public static Method getMethod(Class<?> classObject, String interfaceName, String methodName, Class<?>... paramTypes) {
        try {
            return classObject.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Class " + classObject + " does not implement " + interfaceName);
        }
    }

}
