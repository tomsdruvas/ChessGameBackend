package testUtil;

import java.util.Comparator;

public final class GenericComparators {

    private GenericComparators() {
    }

    public static <T> Comparator<T> notNullComparator() {
        return (actual, expected) -> actual != null ? 0 : 1;
    }
}

