package id.lariss.service.dto.whatsapp.webhook;

import java.util.Objects;

public class Change {

    private Value value;

    private String field;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Change change = (Change) o;
        return Objects.equals(value, change.value) && Objects.equals(field, change.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, field);
    }

    @Override
    public String toString() {
        return "Change{" + "value=" + value + ", field='" + field + '\'' + '}';
    }
}
