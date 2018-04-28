package se.fnord.taggedmessage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTaggedMessage {

    @Test
    public void testFormatToNoTags() {
        TaggedMessage message = new TaggedMessage(Tags.empty(), null);
        StringBuilder sb = new StringBuilder();
        message.formatTo(sb);
        assertEquals("", sb.toString());
    }

    @Test
    public void testFormatToSingleTag() {
        TaggedMessage message = new TaggedMessage(Tags.of("a", "b"), null);
        StringBuilder sb = new StringBuilder();
        message.formatTo(sb);
        assertEquals("a=\"b\"", sb.toString());
    }

    @Test
    public void testFormatToMultiTag() {
        TaggedMessage message = new TaggedMessage(Tags.of("a", "b", "c", "d"), null);
        StringBuilder sb = new StringBuilder();
        message.formatTo(sb);
        assertEquals("a=\"b\" c=\"d\"", sb.toString());
    }

    @Test
    public void testGetFormat() {
        TaggedMessage message = new TaggedMessage(Tags.of("a", "b"), null);
        assertEquals("a=\"b\"", message.getFormat());
    }

    @Test
    public void testGetFormattedMessage() {
        TaggedMessage message = new TaggedMessage(Tags.of("a", "b"), null);
        assertEquals("a=\"b\"", message.getFormattedMessage());
    }
}
