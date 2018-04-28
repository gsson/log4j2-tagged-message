package se.fnord.taggedmessage;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestTags {
    static class Tag {
        private final String key;
        private final String value;

        Tag(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tag tag = (Tag) o;
            return Objects.equals(key, tag.key) &&
                    Objects.equals(value, tag.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return String.format("<%s: \"%s\">", key, value);
        }
    }
    private static Tag tag(String key, String value) {
        return new Tag(key, value);
    }

    private static List<Tag> collectTags(Tags tags) {
        List<Tag> tagList = new ArrayList<>();
        tags.forEach(tagList, (k, v, l) -> l.add(new Tag(k, v)));
        return tagList;
    }

    private static void assertForEach(Tags tags, Tag ... expected) {
        List<Tag> tagList = collectTags(tags);

        assertIterableEquals(asList(expected), tagList);
    }

    @Test
    public void testForEach() {
        Tags.empty()
                .forEach(null, (k, v, s) -> fail("Empty tags should not invoke function"));

        assertForEach(Tags.of("a", "b")
                .add("c", "d")
                .add( "e", "f", "g", "h"),
                tag("a", "b"), tag("c", "d"), tag("e", "f"), tag("g", "h"));
    }

    @Test
    public void testOf() {
        assertForEach(Tags.of("a", "b"), tag("a", "b"));
        assertForEach(Tags.of("a", "b", "c", "d"), tag("a", "b"), tag("c", "d"));
        assertForEach(Tags.of("a", "b", "c", "d", "e", "f"), tag("a", "b"), tag("c", "d"), tag("e", "f"));

    }

    @Test
    public void testAdd() {
        assertForEach(Tags.empty().add("a", "b"), tag("a", "b"));
        assertForEach(Tags.empty().add("a", "b", "c", "d"), tag("a", "b"), tag("c", "d"));
        assertForEach(Tags.empty().add("a", "b", "c", "d", "e", "f"), tag("a", "b"), tag("c", "d"), tag("e", "f"));
    }
}
