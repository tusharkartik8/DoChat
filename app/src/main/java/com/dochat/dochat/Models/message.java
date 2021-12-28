package com.dochat.dochat.Models;

public class message {

    private String messageId, message, senderId;
    private long timestamp;
    private int feeling;

    public message() {
    }

    public message( String message, String senderId, long timestamp) {

        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;

    }

    public String getMessageId() {

        return messageId;
    }

    public void setMessageId(String messageId) {

        this.messageId = messageId;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getSenderId() {

        return senderId;
    }

    public void setSenderId(String senderId) {

        this.senderId = senderId;
    }

    public long getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(long timestamp) {

        this.timestamp = timestamp;
    }

    public int getFeeling() {

        return feeling;
    }

    public void setFeeling(int feeling) {

        this.feeling = feeling;
    }
}
