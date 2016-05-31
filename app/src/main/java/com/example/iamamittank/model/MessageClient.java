package com.example.iamamittank.model;

/**
 * Created by iamamittank on 26-Apr-16.
 */
public class MessageClient {

    private long senderId;
    private long receiverId;
    private String messageBody;
    private String key;

    public MessageClient() {
        super();
    }

    public MessageClient(long senderId, long receiverId, String messageBody, String key) {
        super();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
        this.key = key;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}