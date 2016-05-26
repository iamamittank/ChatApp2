package com.example.iamamittank.model;

/**
 * Created by iamamittank on 17-May-16.
 */
public class Relation {

    private long relationId;
    private long id;
    private long userId;
    private int status;

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
