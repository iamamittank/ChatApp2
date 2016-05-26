package com.example.iamamittank.model;

public class ChatListItem {

    private long user_id;
    private String name;
    private String last_message;
    private boolean isOnline;

    public ChatListItem() {
    }

    public ChatListItem(long user_id, String userName, String lastMessage, boolean isOnline) {
        this.user_id = user_id;
        this.name = userName;
        this.last_message = lastMessage;
        this.isOnline = isOnline;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
