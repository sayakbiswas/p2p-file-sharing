package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.nio.ByteOrder;
/**
 * Created by sayak on 10/27/17.
 */
public class PieceMessage extends Message {
    public static byte[] getMessage(int pieceIndex, byte[] bytes){
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
            i++;
        }
        int j = 0;
        while(j < bytes.length) {
            message[i + j] = bytes[j];
            j++;
        }
        return message;
    }

    public static byte[] retrievePieceData(byte[] incomingMessage) {
        byte[] pieceData = new byte[incomingMessage.length - 9];
        System.arraycopy(incomingMessage, 9, pieceData, 0, pieceData.length);
        return pieceData;
    }

    public static int getIndexOfPiece(byte[] incomingMessage) {
        byte[] pieceIndex = new byte[4];
        System.arraycopy(incomingMessage, 5, pieceIndex, 0, 4);
        return ByteBuffer.wrap(pieceIndex).getInt();
    }
}
