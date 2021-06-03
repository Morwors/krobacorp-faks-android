package com.example.krobachat.model;

public class Message {
    private String message;
    private User sentBy;
    private String createdAt;

    public Message(String message, User sentBy, String createdAt) {
        this.message = message;
        this.sentBy = sentBy;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sentBy=" + sentBy +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
