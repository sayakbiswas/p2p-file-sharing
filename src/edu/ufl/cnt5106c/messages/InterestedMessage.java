package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;

/**
 * Created by sayak on 10/26/17.
 */
public class InterestedMessage extends Message {
    public static byte[] getMessage() {
        int messageLength = 1;
        int messageType = 2;
        byte[] message = new byte[5];
        byte[] messageLengthField = ByteBuffer.allocate(4).putInt(messageLength).array();
        System.arraycopy(messageLengthField, 0, message, 0, messageLengthField.length);
        message[messageLengthField.length] = (byte) messageType;
        return message;
    }
}
