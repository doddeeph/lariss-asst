package id.lariss.service.dto.whatsapp.webhook;

import java.util.Objects;

public class Message {

    private String from;
    private String id;
    private String timestamp;
    private Text text;
    private String type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return (
            Objects.equals(from, message.from) &&
            Objects.equals(id, message.id) &&
            Objects.equals(timestamp, message.timestamp) &&
            Objects.equals(text, message.text) &&
            Objects.equals(type, message.type)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, id, timestamp, text, type);
    }

    @Override
    public String toString() {
        return (
            "Message{" +
            "from='" +
            from +
            '\'' +
            ", id='" +
            id +
            '\'' +
            ", timestamp='" +
            timestamp +
            '\'' +
            ", text=" +
            text +
            ", type='" +
            type +
            '\'' +
            '}'
        );
    }
}
