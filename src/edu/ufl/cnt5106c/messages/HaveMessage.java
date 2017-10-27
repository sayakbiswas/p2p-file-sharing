package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by sayak on 10/26/17.
 */
public class HaveMessage extends Message {
    public static byte[] getHaveMessage(int pieceIndex) {
        int messageLength = 5;
        int messageType = 4;
        byte[] message = new byte[4 + messageLength];
        byte[] messageLengthField = ByteBuffer.allocate(4).putInt(messageLength).array();
        int i = 0;
        for (i = 0; i < 4; i++) {
            message[i] = messageLengthField[i];
        }
        message[i++] = (byte) messageType;
        byte[] pieceIndexField = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(pieceIndex).array();
        while(i < 9) {
            message[i] = pieceIndexField[i - 5];
            i++;
        }
        return message;
    }
}
