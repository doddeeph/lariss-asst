package id.lariss.service.dto.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class Request {

    private String object;

    @JsonProperty("entry")
    private List<Entry> entries;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Request that = (Request) o;
        return Objects.equals(object, that.object) && Objects.equals(entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, entries);
    }

    @Override
    public String toString() {
        return "Request{" + "object='" + object + '\'' + ", entries=" + entries + '}';
    }
}
