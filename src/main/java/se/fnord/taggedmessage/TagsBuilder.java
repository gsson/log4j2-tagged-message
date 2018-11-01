package se.fnord.taggedmessage;

import org.apache.logging.log4j.util.Constants;

import java.util.Arrays;
import java.util.Map;

class TagsBuilder {
    private static final int INITIAL_SIZE = 8;
    private static final ThreadLocal<TagsBuilder> BUILDER = Constants.ENABLE_THREADLOCALS ? ThreadLocal.withInitial(TagsBuilder::new) : null;

    private String[] keys;
    private Object[] values;
    private int n;

    TagsBuilder() {
        keys = new String[INITIAL_SIZE];
        values = new Object[INITIAL_SIZE];
        n = 0;
    }

    TagsBuilder add(String key, Object value) {
        if (n == keys.length) {
            ensureCapacity(n + 1);
        }
        addInternal(key, value);
        return this;
    }

    private void addInternal(String key, Object value) {
        keys[n] = key;
        values[n] = value;
        n++;
    }

    void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > 0 && minimumCapacity - keys.length > 0) {
            int newCapacity = newCapacity(minimumCapacity);
            keys = Arrays.copyOf(keys, newCapacity);
            values = Arrays.copyOf(values, newCapacity);
        }
    }

    private int newCapacity(int minimumCapacity) {
        int newCapacity = keys.length << 2;
        // Handles both overflow in shift and when a doubling is not sufficient
        if (newCapacity - minimumCapacity >= 0) {
            return newCapacity;
        }
        return minimumCapacity;
    }

    private void clear() {
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
        n = 0;
    }

    private Tags createTags(Tags next) {
        switch (n) {
            case 0:
                return next;
            case 1:
                return next.add(keys[0], values[0]);
            default:
                return new TagsN(
                        Arrays.copyOf(keys, n),
                        Arrays.copyOf(values, n),
                        next);
        }
    }

    TagsBuilder addFromMap(Map<String, ? extends Object> map) {
        int size = map.size();
        ensureCapacity(n + size);
        map.forEach(this::addInternal);
        return this;
    }

    Tags build() {
        Tags tags = createTags(Tags.empty());
        clear();
        return tags;
    }

    Tags build(Tags next) {
        Tags tags = createTags(next);
        clear();
        return tags;
    }

    static TagsBuilder get() {
        if (Constants.ENABLE_THREADLOCALS) {
            return BUILDER.get();
        }
        else {
            return new TagsBuilder();
        }
    }
}
