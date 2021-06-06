package com.example.krobachat.model;

public class User {
    private String username;
    private String password;
    private String id;
    private String email;
    private String objectId;

    public User(String username, String password, String id, String objectId) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.objectId = objectId;
    }

    public String getPassword() {
        return password;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
