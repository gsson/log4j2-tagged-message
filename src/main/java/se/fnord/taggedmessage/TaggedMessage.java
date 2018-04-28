package se.fnord.taggedmessage;

import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Chars;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@AsynchronouslyFormattable
public class TaggedMessage implements Message, StringBuilderFormattable {
    private static final long serialVersionUID = 1L;
    private final Tags tags;
    private final Throwable throwable;

    public TaggedMessage(Tags tags, Throwable throwable) {
        this.tags = tags;
        this.throwable = throwable;
    }

    private static void renderTag(String key, String value, StringBuilder sb) {
        sb.ensureCapacity(key.length() + value.length() + 4);
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(key)
                .append(Chars.EQ)
                .append(Chars.DQUOTE)
                .append(value)
                .append(Chars.DQUOTE);
    }

    private static void renderTags(StringBuilder sb, Tags tags) {
        tags.forEach(sb, TaggedMessage::renderTag);
    }

    public Tags getTags() {
        return tags;
    }

    @Override
    public String getFormattedMessage() {
        StringBuilder buffer = new StringBuilder();
        renderTags(buffer, tags);
        return buffer.toString();
    }

    @Override
    public String getFormat() {
        return getFormattedMessage();
    }

    @Override
    public Object[] getParameters() {
        return null;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public void formatTo(StringBuilder buffer) {
        renderTags(buffer, tags);
    }
}
