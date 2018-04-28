package se.fnord.taggedmessage;

import org.apache.logging.log4j.util.TriConsumer;

import java.io.Serializable;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public interface Tags extends Serializable {
    default <T> void forEach(T state, TriConsumer<String, String, T> tagConsumer) {
        forEachGroup(g -> g.forEachTagInGroup(state, tagConsumer));
    }

    void forEachGroup(Consumer<Tags> collectTo);
    <T> void forEachTagInGroup(T state, TriConsumer<String, String, T> tagConsumer);

    default Tags add(String key, String value) {
        return new Tags1(key, value, this);
    }

    default Tags add(String key1, String value1, String key2, String value2) {
        return new TagsN(new String[] { key1, key2 }, new String[] { value1, value2 }, this);
    }

    default Tags add(String key1, String value1, String key2, String value2, String key3, String value3) {
        return new TagsN(new String[] { key1, key2, key3 }, new String[] { value1, value2, value3 }, this);
    }

    static Tags of(String key, String value) {
        return new Tags1(key, value, empty());
    }

    static Tags of(String key1, String value1, String key2, String value2) {
        return new TagsN(new String[] { key1, key2 }, new String[] { value1, value2 }, empty());
    }

    static Tags of(String key1, String value1, String key2, String value2, String key3, String value3) {
        return new TagsN(new String[] { key1, key2, key3 }, new String[] { value1, value2, value3 }, empty());
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
    public <T> void forEachTagInGroup(T state, TriConsumer<String, String, T> tagConsumer) {
    }
}

class Tags1 implements Tags {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final String value;

    private final Tags next;

    Tags1(String key, String value, Tags next) {
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
    public <T> void forEachTagInGroup(T state, TriConsumer<String, String, T> tagConsumer) {
        tagConsumer.accept(key, value, state);
    }
}

class TagsN implements Tags {
    private static final long serialVersionUID = 1L;
    private final String[] keys;
    private final String[] values;

    private final Tags next;

    TagsN(String[] keys, String[] values, Tags next) {
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
    public <T> void forEachTagInGroup(T state, TriConsumer<String, String, T> tagConsumer) {
        for (int i = 0; i < keys.length; i++) {
            tagConsumer.accept(keys[i], values[i], state);
        }
    }
}
