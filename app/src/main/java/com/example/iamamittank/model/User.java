package com.example.iamamittank.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by iamamittank on 26-Apr-16.
 */
public class User implements Parcelable {

    private long id;
    private String name;
    private String email;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public User() {
    }

    public User(long id, String name, String email) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.publicKey = null;
        this.privateKey = null;
    }

    public User(long id, String name, String email, PublicKey publicKey, PrivateKey privateKey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
    }
}
