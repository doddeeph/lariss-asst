package id.lariss.service.dto.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Metadata {

    @JsonProperty("display_phone_number")
    private String displayPhoneNumber;

    @JsonProperty("phone_number_id")
    private String phoneNumberId;

    public String getDisplayPhoneNumber() {
        return displayPhoneNumber;
    }

    public void setDisplayPhoneNumber(String displayPhoneNumber) {
        this.displayPhoneNumber = displayPhoneNumber;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(displayPhoneNumber, metadata.displayPhoneNumber) && Objects.equals(phoneNumberId, metadata.phoneNumberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayPhoneNumber, phoneNumberId);
    }

    @Override
    public String toString() {
        return "Metadata{" + "displayPhoneNumber='" + displayPhoneNumber + '\'' + ", phoneNumberId='" + phoneNumberId + '\'' + '}';
    }
}
