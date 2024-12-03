package id.lariss.service.dto;

import java.util.Objects;

public class TwilioWebhookDTO {

    private String apiVersion;
    private String smsSid;
    private String smsStatus;
    private String smsMessageSid;
    private String profileName;
    private Integer numSegments;
    private String from;
    private String waId;
    private String messageSid;
    private String accountSid;
    private String buttonText;
    private Integer referralNumMedia;
    private String to;
    private String body;
    private String messageType;
    private String buttonPayload;
    private Integer numMedia;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getSmsSid() {
        return smsSid;
    }

    public void setSmsSid(String smsSid) {
        this.smsSid = smsSid;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getSmsMessageSid() {
        return smsMessageSid;
    }

    public void setSmsMessageSid(String smsMessageSid) {
        this.smsMessageSid = smsMessageSid;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getNumSegments() {
        return numSegments;
    }

    public void setNumSegments(Integer numSegments) {
        this.numSegments = numSegments;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWaId() {
        return waId;
    }

    public void setWaId(String waId) {
        this.waId = waId;
    }

    public String getMessageSid() {
        return messageSid;
    }

    public void setMessageSid(String messageSid) {
        this.messageSid = messageSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public Integer getReferralNumMedia() {
        return referralNumMedia;
    }

    public void setReferralNumMedia(Integer referralNumMedia) {
        this.referralNumMedia = referralNumMedia;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getButtonPayload() {
        return buttonPayload;
    }

    public void setButtonPayload(String buttonPayload) {
        this.buttonPayload = buttonPayload;
    }

    public Integer getNumMedia() {
        return numMedia;
    }

    public void setNumMedia(Integer numMedia) {
        this.numMedia = numMedia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TwilioWebhookDTO that = (TwilioWebhookDTO) o;
        return (
            Objects.equals(apiVersion, that.apiVersion) &&
            Objects.equals(smsSid, that.smsSid) &&
            Objects.equals(smsStatus, that.smsStatus) &&
            Objects.equals(smsMessageSid, that.smsMessageSid) &&
            Objects.equals(profileName, that.profileName) &&
            Objects.equals(numSegments, that.numSegments) &&
            Objects.equals(from, that.from) &&
            Objects.equals(waId, that.waId) &&
            Objects.equals(messageSid, that.messageSid) &&
            Objects.equals(accountSid, that.accountSid) &&
            Objects.equals(buttonText, that.buttonText) &&
            Objects.equals(referralNumMedia, that.referralNumMedia) &&
            Objects.equals(to, that.to) &&
            Objects.equals(body, that.body) &&
            Objects.equals(messageType, that.messageType) &&
            Objects.equals(buttonPayload, that.buttonPayload) &&
            Objects.equals(numMedia, that.numMedia)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            apiVersion,
            smsSid,
            smsStatus,
            smsMessageSid,
            profileName,
            numSegments,
            from,
            waId,
            messageSid,
            accountSid,
            buttonText,
            referralNumMedia,
            to,
            body,
            messageType,
            buttonPayload,
            numMedia
        );
    }

    @Override
    public String toString() {
        return (
            "TwilioWebhookDTO{" +
            "apiVersion='" +
            apiVersion +
            '\'' +
            ", smsSid='" +
            smsSid +
            '\'' +
            ", smsStatus='" +
            smsStatus +
            '\'' +
            ", smsMessageSid='" +
            smsMessageSid +
            '\'' +
            ", profileName='" +
            profileName +
            '\'' +
            ", numSegments=" +
            numSegments +
            ", from='" +
            from +
            '\'' +
            ", waId='" +
            waId +
            '\'' +
            ", messageSid='" +
            messageSid +
            '\'' +
            ", accountSid='" +
            accountSid +
            '\'' +
            ", buttonText='" +
            buttonText +
            '\'' +
            ", referralNumMedia=" +
            referralNumMedia +
            ", to='" +
            to +
            '\'' +
            ", body='" +
            body +
            '\'' +
            ", messageType='" +
            messageType +
            '\'' +
            ", buttonPayload='" +
            buttonPayload +
            '\'' +
            ", numMedia=" +
            numMedia +
            '}'
        );
    }
}
