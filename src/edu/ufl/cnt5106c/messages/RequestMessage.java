package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by sayak on 10/27/17.
 */
public class RequestMessage extends Message {
    public static byte[] getMessage(int pieceIndex) {
        int messageLength = 5;
        int messageType = 6;
        byte[] message = new byte[4 + messageLength];
        byte[] messageLengthField = ByteBuffer.allocate(4).putInt(messageLength).array();
        int i = 0;
        for (i = 0; i < 4; i++) {
            message[i] = messageLengthField[i];
        }
        message[i++] = (byte) messageType;
        byte pieceIndexField[]=ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(pieceIndex).array();
        while(i < 9) {
            message[i] = pieceIndexField[i - 5];
            i++;
        }

        return message;
    }
    //Added By Ankit
    public static int getIndex(byte msg[]) // to get piece index
    {
        return ByteBuffer.wrap(Arrays.copyOfRange(msg, 5, 9)).order(ByteOrder.BIG_ENDIAN).getInt();
    }
}
