package se.fnord.taggedmessage;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Stream;

class TagsFactory {
    private enum TagType {
        LONG, DOUBLE, BOOLEAN, TEXT
    }
    private static final Map<Class<?>, TagType> TAG_TYPE_MAP;
    static {
        IdentityHashMap<Class<?>, TagType> typeMap = new IdentityHashMap<>();
        typeMap.put(Long.class, TagType.LONG);
        typeMap.put(Integer.class, TagType.LONG);
        typeMap.put(Short.class, TagType.LONG);
        typeMap.put(Byte.class, TagType.LONG);
        typeMap.put(Float.class, TagType.DOUBLE);
        typeMap.put(Double.class, TagType.DOUBLE);
        typeMap.put(Boolean.class, TagType.BOOLEAN);
        TAG_TYPE_MAP = typeMap;
    }

    static Tags create(CharSequence key, Object value, Tags next) {
        if (value == null) {
            return new NullTags1(key, next);
        }
        switch (TAG_TYPE_MAP.getOrDefault(value.getClass(), TagType.TEXT)) {
            case LONG:
                return new LongTags1(key, ((Number) value).longValue(), next);
            case DOUBLE:
                return new DoubleTags1(key, ((Number) value).doubleValue(), next);
            case BOOLEAN:
                return new BooleanTags1(key, (Boolean) value, next);
            default:
                return new Tags1(key, value.toString(), next);
        }
    }

    static Tags create(CharSequence[] keys, Object[] values, Tags next) {
        Object[] safeValues = Stream.of(values)
                .map(TagsFactory::normaliseObjectValue)
                .toArray(Object[]::new);
        return new TagsN(keys, safeValues, next);
    }

    static Object normaliseObjectValue(Object value) {
        if (value == null) {
            return null;
        }
        return TAG_TYPE_MAP.containsKey(value.getClass()) ? value : value.toString();
    }
}
