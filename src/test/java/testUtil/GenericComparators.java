package testUtil;

import java.util.Comparator;

public final class GenericComparators {

    public static final String[] DYNAMIC_FIELDS = {
        "id"
    };


    private GenericComparators() {
    }

    public static <T> Comparator<T> notNullComparator() {
        return (actual, expected) -> actual != null ? 0 : 1;
    }
}

