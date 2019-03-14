package se.fnord.taggedmessage;

import java.util.IdentityHashMap;
import java.util.Map;

class TagsFactory {
    private static final Map<Class<?>, Boolean> SAFE_TYPES;

    static {
        IdentityHashMap<Class<?>, Boolean> safeTypes = new IdentityHashMap<>();
        safeTypes.put(String.class, Boolean.TRUE);
        safeTypes.put(Long.class, Boolean.TRUE);
        safeTypes.put(Integer.class, Boolean.TRUE);
        safeTypes.put(Short.class, Boolean.TRUE);
        safeTypes.put(Byte.class, Boolean.TRUE);
        safeTypes.put(Float.class, Boolean.TRUE);
        safeTypes.put(Double.class, Boolean.TRUE);
        safeTypes.put(Boolean.class, Boolean.TRUE);
        SAFE_TYPES = safeTypes;
    }

    static Object normaliseObjectValue(Object value) {
        if (value == null) {
            return null;
        }
        return SAFE_TYPES.containsKey(value.getClass()) ? value : value.toString();
    }
}
