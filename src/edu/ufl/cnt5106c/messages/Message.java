package edu.ufl.cnt5106c.messages;

/**
 * Created by sayak on 10/26/17.
 */
public class Message {
    private int messageLength;
    private byte messageType;
    private byte[] messagePayload;

    public Message(byte messageType) {
        this.messageType = messageType;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte[] getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(byte[] messagePayload) {
        this.messagePayload = messagePayload;
    }
}
