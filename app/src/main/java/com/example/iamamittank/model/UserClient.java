package com.example.iamamittank.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserClient implements Parcelable {

    private long id;
    private String name;
    private String email;
    private String publicKey;
    private String privateKey;

    public UserClient() {
    }

    public UserClient(long id, String name, String email, String publicKey, String privateKey) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    protected UserClient(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        publicKey = in.readString();
        privateKey = in.readString();
    }

    public static final Creator<UserClient> CREATOR = new Creator<UserClient>() {
        @Override
        public UserClient createFromParcel(Parcel source) {
            return new UserClient(source);
        }

        @Override
        public UserClient[] newArray(int size) {
            return new UserClient[size];
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
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getPrivateKey() {
        return privateKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
