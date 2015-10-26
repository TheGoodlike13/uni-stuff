package eu.goodlike.utils;

import java.util.Collections;
import java.util.List;

public final class CollectionUtils {

    //No instantiation
    private CollectionUtils() {throw new AssertionError();}

    //Returns a sublist view of elements from start to finish, both exclusive
    //If start >= finish, list is empty
    public static <T> List<T> getPart(List<T> list, T start, T finish) {
        int startIndex = list.indexOf(start);
        if (startIndex < 0)
            return Collections.<T>emptyList();

        List<T> subList = list.subList(startIndex + 1, list.size());
        int endIndex = subList.indexOf(finish);
        return endIndex <= 0 ? Collections.<T>emptyList() : subList.subList(0, endIndex);
    }

    public static <T> T getFirst(List<T> list, T start, T finish) {
        return getPart(list, start, finish).get(0);
    }
}
