package com.example.iamamittank.model;

/**
 * Created by iamamittank on 30-May-16.
 */
public class SessionClient {

    private long user_id;
    private String token;

    public SessionClient() {
    }

    public SessionClient(long user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
