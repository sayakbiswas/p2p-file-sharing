package edu.ufl.cnt5106c.messages;

/**
 * Created by sayak on 10/26/17.
 */
public class Message {
    private int messageLengthField;
    private byte messageTypeField;
    private byte[] messagePayloadField;

    public Message() {

    }

    public Message(byte messageTypeField) {
        this.messageTypeField = messageTypeField;
    }

    public int getMessageLengthField() {
        return messageLengthField;
    }

    public void setMessageLengthField(int messageLengthField) {
        this.messageLengthField = messageLengthField;
    }

    public byte getMessageTypeField() {
        return messageTypeField;
    }

    public void setMessageTypeField(byte messageTypeField) {
        this.messageTypeField = messageTypeField;
    }

    public byte[] getMessagePayloadField() {
        return messagePayloadField;
    }

    public void setMessagePayloadField(byte[] messagePayloadField) {
        this.messagePayloadField = messagePayloadField;
    }
}
