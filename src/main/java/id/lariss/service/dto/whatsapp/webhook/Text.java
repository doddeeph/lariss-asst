package id.lariss.service.dto.whatsapp.webhook;

import java.util.Objects;

public class Text {

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(body, text.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(body);
    }

    @Override
    public String toString() {
        return "Text{" + "body='" + body + '\'' + '}';
    }
}
