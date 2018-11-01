package se.fnord.taggedmessage;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public interface Tags extends Serializable {


    default <T> void forEach(T state, TagConsumer<T> tagConsumer) {
        forEachGroup(g -> g.forEachTagInGroup(state, tagConsumer));
    }

    void forEachGroup(Consumer<Tags> collectTo);
    <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer);

    default Tags add(String key, Object value) {
        return TagsFactory.create(key, value, this);
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
        return TagsFactory.create(new CharSequence[] { key1, key2 }, new Object[] { value1, value2 }, this);
    }

    default Tags add(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        return TagsFactory.create(new CharSequence[] { key1, key2, key3 }, new Object[] { value1, value2, value3 }, this);
    }

    default Tags addFromMap(Map<String, ? extends Object> map) {
        return TagsBuilder.get()
                .addFromMap(map)
                .build();
    }

    static Tags of(String key, Object value) {
        return TagsFactory.create(key, value, empty());
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
        return TagsFactory.create(new CharSequence[] { key1, key2 }, new Object[] { value1, value2 }, empty());
    }

    static Tags of(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        return TagsFactory.create(new CharSequence[] { key1, key2, key3 }, new Object[] { value1, value2, value3 }, empty());
    }

    static Tags fromMap(Map<String, ? extends Object> map) {
        return TagsBuilder.get()
                .addFromMap(map)
                .build();
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
    private final CharSequence key;
    private final Object value;

    private final Tags next;

    Tags1(CharSequence key, Object value, Tags next) {
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
    private final CharSequence key;
    private final long value;

    private final Tags next;

    LongTags1(CharSequence key, long value, Tags next) {
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
    private final CharSequence key;
    private final double value;

    private final Tags next;

    DoubleTags1(CharSequence key, double value, Tags next) {
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
    private final CharSequence key;
    private final boolean value;

    private final Tags next;

    BooleanTags1(CharSequence key, boolean value, Tags next) {
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

class NullTags1 implements Tags {
    private static final long serialVersionUID = 1L;
    private final CharSequence key;

    private final Tags next;

    NullTags1(CharSequence key, Tags next) {
        this.key = key;
        this.next = next;
    }

    @Override
    public void forEachGroup(Consumer<Tags> collectTo) {
        next.forEachGroup(collectTo);
        collectTo.accept(this);
    }

    @Override
    public <T> void forEachTagInGroup(T state, TagConsumer<T> tagConsumer) {
        tagConsumer.nullTag(key, state);
    }
}

class TagsN implements Tags {
    private static final long serialVersionUID = 1L;
    private final CharSequence[] keys;
    private final Object[] values;

    private final Tags next;

    TagsN(CharSequence[] keys, Object[] values, Tags next) {
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
