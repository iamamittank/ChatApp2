package com.example.iamamittank.model;

/**
 * Created by iamamittank on 26-Apr-16.
 */
public class User {

    private long id;
    private String name;
    private String email;

    public User() {
        super();
    }

    public User(long id, String name, String email) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
