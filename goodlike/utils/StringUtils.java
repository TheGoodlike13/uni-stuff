package com.goodlike.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final String MAX_INT_STR = String.valueOf(Integer.MAX_VALUE);
    private static final String MIN_INT_STR = String.valueOf(Integer.MIN_VALUE);
    private static final int MAX_INT_VALUE_LENGTH = MAX_INT_STR.length();
    private static final int MIN_INT_VALUE_LENGTH = MIN_INT_STR.length();
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?[0-9]+$");
    private static final char NEGATIVE_INDICATOR = '-';
    private static final int DEFAULT_PARSE_FAILURE = 0;

    //No instantiation
    private StringUtils() {throw new AssertionError();}

    //Concatenates all String objects passed as parameters
    public static String concat(String... allStrings) {
        StringBuilder result = new StringBuilder();
        for (String string: allStrings)
            result.append(string);
        return result.toString();
    }

    //Concatenates all String objects passed as parameters n times
    public static String concat(int n, String... allStrings) {
        StringBuilder result = new StringBuilder();
        while (n-- > 0)
            for (String string: allStrings)
                result.append(string);
        return result.toString();
    }

    //Returns true if the string holds a parsable integer
    public static boolean isInteger(String string) {
        return INTEGER_PATTERN.matcher(string).matches() && (string.charAt(0) == NEGATIVE_INDICATOR
                ? string.length() == MIN_INT_VALUE_LENGTH
                ? string.compareTo(MIN_INT_STR) <= 0
                : string.length() < MIN_INT_VALUE_LENGTH
                : string.length() == MAX_INT_VALUE_LENGTH
                ? string.compareTo(MAX_INT_STR) <= 0
                : string.length() < MAX_INT_VALUE_LENGTH);
    }

    //Returns an int representation of an object, or a constant if it cannot be done
    @SuppressWarnings("unused")
    public static int parseInt(Object obj) {
        String string = String.valueOf(obj);
        return isInteger(string) ? Integer.parseInt(string) : DEFAULT_PARSE_FAILURE;
    }

    //Returns a String representation of an object
    @SuppressWarnings("unused")
    public static String parseString(Object obj) {
        return String.valueOf(obj);
    }

    public static int spaceSplitCount(CharSequence cs) {
        return spaceSplit(cs).size();
    }

    public static List<String> spaceSplit(CharSequence cs) {
        String s = cs.toString();
        List<String> list = new ArrayList<>();
        String trim = s.trim();
        int p = 0;
        int start = 0;
        while (p < trim.length()) {
            if (trim.charAt(p) <= ' ') {
                if (start < p)
                    list.add(trim.substring(start, p));
                start = p + 1;
            }
            p++;
        }
        if (start < trim.length())
            list.add(trim.substring(start));
        return list;
    }


}
