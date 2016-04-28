package com.example.iamamittank.model;

/**
 * Created by iamamittank on 26-Apr-16.
 */
public class Message {

    private int messageId;
    private int senderId;
    private int receiverId;
    private String messageBody;

    public Message() {
        super();
    }

    public Message(int messageId, int senderId, int receiverId, String messageBody) {
        super();
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
    }

    public int getMessageId() {
        return messageId;
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    public int getSenderId() {
        return senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    public int getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
    public String getMessageBody() {
        return messageBody;
    }
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
