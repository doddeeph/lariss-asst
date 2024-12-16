package id.lariss.service.dto.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class Value {

    @JsonProperty("messaging_product")
    private String messaging_Product;

    private Metadata metadata;

    private List<Contact> contacts;

    private List<Message> messages;

    public String getMessaging_Product() {
        return messaging_Product;
    }

    public void setMessaging_Product(String messaging_Product) {
        this.messaging_Product = messaging_Product;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Value value = (Value) o;
        return (
            Objects.equals(messaging_Product, value.messaging_Product) &&
            Objects.equals(metadata, value.metadata) &&
            Objects.equals(contacts, value.contacts) &&
            Objects.equals(messages, value.messages)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(messaging_Product, metadata, contacts, messages);
    }

    @Override
    public String toString() {
        return (
            "Value{" +
            "messaging_Product='" +
            messaging_Product +
            '\'' +
            ", metadata=" +
            metadata +
            ", contacts=" +
            contacts +
            ", messages=" +
            messages +
            '}'
        );
    }
}
