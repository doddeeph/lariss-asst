package id.lariss.service.dto.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Contact {

    private Profile profile;

    @JsonProperty("wa_id")
    private String waId;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getWaId() {
        return waId;
    }

    public void setWaId(String waId) {
        this.waId = waId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(profile, contact.profile) && Objects.equals(waId, contact.waId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, waId);
    }

    @Override
    public String toString() {
        return "Contact{" + "profile=" + profile + ", waId='" + waId + '\'' + '}';
    }
}
