package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;

/**
 * Created by sayak on 10/26/17.
 */
public class UnchokeMessage extends Message {
    public static byte[] getUnchokeMessage() {
        int messageLength = 1;
        int messageType = 1;
        byte[] message = new byte[5];
        byte[] messageLengthField = ByteBuffer.allocate(4).putInt(messageLength).array();
        int i = 0;
        for (i = 0; i < 4; i++) {
            message[i] = messageLengthField[i];
        }
        message[i] = (byte) messageType;
        return message;
    }
}
