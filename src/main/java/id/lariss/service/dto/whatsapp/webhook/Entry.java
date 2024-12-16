package id.lariss.service.dto.whatsapp.webhook;

import java.util.List;
import java.util.Objects;

public class Entry {

    private String id;

    private List<Change> changes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id) && Objects.equals(changes, entry.changes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, changes);
    }

    @Override
    public String toString() {
        return "Entry{" + "id='" + id + '\'' + ", changes=" + changes + '}';
    }
}
