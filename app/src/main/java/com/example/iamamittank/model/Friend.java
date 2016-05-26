package com.example.iamamittank.model;

/**
 * Created by iamamittank on 17-May-16.
 */
public class Friend {

    private Long id;
    private String userName;
    private String userEmail;

    public Friend() {

    }

    public Friend(Long id, String name, String email) {
        this.id = id;
        this.userName = name;
        this.userEmail= email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
