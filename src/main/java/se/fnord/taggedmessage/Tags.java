package se.fnord.taggedmessage;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public interface Tags extends Serializable {
    default <T> void forEach(T state, TagConsumer<T> tagConsumer) {
        forEachGroup(g -> g.forEachTagInGroup(state, tagConsumer));
    }

    void forEachGroup(Consumer<Tags> collectTo);
    <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer);

    default Tags add(String key, Object value) {
        return new Tags1(key, value, this);
    }

    default Tags add(String key, long value) {
        return new LongTags1(key, value, this);
    }
    default Tags add(String key, double value) {
        return new DoubleTags1(key, value, this);
    }
    default Tags add(String key, boolean value) {
        return new BooleanTags1(key, value, this);
    }

    default Tags add(String key1, Object value1, String key2, Object value2) {
        return new TagsN(new String[] { key1, key2 }, new Object[] { value1, value2 }, this);
    }

    default Tags add(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        return new TagsN(new String[] { key1, key2, key3 }, new Object[] { value1, value2, value3 }, this);
    }

    default Tags add(Map<String, ?> tags) {
        return TagsN.fromMap(tags, this);
    }

    static Tags of(String key, Object value) {
        return new Tags1(key, value, empty());
    }

    static Tags of(String key, long value) {
        return new LongTags1(key, value, empty());
    }

    static Tags of(String key, double value) {
        return new DoubleTags1(key, value, empty());
    }

    static Tags of(String key, boolean value) {
        return new BooleanTags1(key, value, empty());
    }

    static Tags of(String key1, Object value1, String key2, Object value2) {
        return new TagsN(new String[] { key1, key2 }, new Object[] { value1, value2 }, empty());
    }

    static Tags of(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        return new TagsN(new String[] { key1, key2, key3 }, new Object[] { value1, value2, value3 }, empty());
    }

    static Tags of(Map<String, ?> tags) {
        return TagsN.fromMap(tags, empty());
    }

    static Tags empty() {
        return Tags0.EMPTY;
    }
}


class Tags0 implements Tags {
    private static final long serialVersionUID = 1L;
    static final Tags0 EMPTY = new Tags0();

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
    }
}

class Tags1 implements Tags {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final Object value;

    private final Tags next;

    Tags1(String key, Object value, Tags next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
        next.forEachGroup(collectTo);
        collectTo.accept(this);
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
        tagConsumer.objectTag(key, value, state);
    }
}

class LongTags1 implements Tags {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final long value;

    private final Tags next;

    LongTags1(String key, long value, Tags next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
        next.forEachGroup(collectTo);
        collectTo.accept(this);
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
        tagConsumer.longTag(key, value, state);
    }
}

class DoubleTags1 implements Tags {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final double value;

    private final Tags next;

    DoubleTags1(String key, double value, Tags next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
        next.forEachGroup(collectTo);
        collectTo.accept(this);
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
        tagConsumer.doubleTag(key, value, state);
    }
}

class BooleanTags1 implements Tags {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final boolean value;

    private final Tags next;

    BooleanTags1(String key, boolean value, Tags next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
        next.forEachGroup(collectTo);
        collectTo.accept(this);
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
        tagConsumer.booleanTag(key, value, state);
    }
}

class TagsN implements Tags {
    private static final long serialVersionUID = 1L;
    private final String[] keys;
    private final Object[] values;

    private final Tags next;

    static Tags fromMap(Map<String, ?> map, Tags next) {
        if (map.isEmpty()) {
            return next;
        }
        int size = map.size();
        // TODO: size == 1 should return a Tags1
        String[] tagNames = new String[size];
        Object[] tagValues = new Object[size];
        int i = 0;
        for (Map.Entry<String, ?> e: map.entrySet()) {
            tagNames[i] = e.getKey();
            tagValues[i++] = e.getValue();
        }
        return new TagsN(tagNames, tagValues, next);
    }

    TagsN(String[] keys, Object[] values, Tags next) {
        this.keys = keys;
        this.values = values;
        this.next = next;
    }

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
        next.forEachGroup(collectTo);
        collectTo.accept(this);
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
        for (int i = 0; i < keys.length; i++) {
            tagConsumer.objectTag(keys[i], values[i], state);
        }
    }
}
