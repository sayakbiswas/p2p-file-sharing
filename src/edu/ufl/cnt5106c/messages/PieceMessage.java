package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;

/**
 * Created by sayak on 10/27/17.
 */
public class PieceMessage extends Message {
    public static byte[] getPieceMessage(int pieceIndex, byte[] bytes){
        int messageLength = 1 + 4 + bytes.length;
        byte messageType = 7;
        byte message[] = new byte[(4 + messageLength)];
        byte[] messageLengthField = ByteBuffer.allocate(4).putInt(messageLength).array();
        int i;
        for(i = 0; i < 4; i++) {
            message[i] = messageLengthField[i];
        }

        message[i++] = messageType;
        byte[] pieceIndexField = ByteBuffer.allocate(4).putInt(pieceIndex).array();
        while(i < 9) {
            message[i] = pieceIndexField[i - pieceIndexField.length - 1];
        }
        int j = 0;
        while(j < bytes.length) {
            message[i + j] = bytes[j];
        }
        return message;
    }
}
