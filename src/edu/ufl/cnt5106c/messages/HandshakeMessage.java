package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;

/**
 * Created by sayak on 11/25/17.
 */
public class HandshakeMessage {
    private static final String HANDSHAKE_HEADER = "P2PFILESHARINGPROJ";

    public static byte[] getMessage(int peerId) {
        byte[] message = new byte[32];
        byte[] handshakeHeaderBytes = HANDSHAKE_HEADER.getBytes();
        int handshakeHeaderLength = handshakeHeaderBytes.length;
        System.arraycopy(handshakeHeaderBytes, 0, message, 0, handshakeHeaderLength);
        for(int i = handshakeHeaderLength; i < handshakeHeaderLength + 10; i++) {
            message[i] = 0;
        }
        byte[] messagePeerIdField = ByteBuffer.allocate(4).putInt(peerId).array();
        for(int i = 0; i < 4; i++) {
            message[handshakeHeaderLength + 10 + i] = messagePeerIdField[i];
        }

        return message;
    }

    public static boolean checkForIncomingHandshake(byte[] incomingMessage) {
        if(incomingMessage.length != 32) {
            return false;
        }

        byte[] handshakeHeaderField = new byte[18];
        System.arraycopy(incomingMessage, 0, handshakeHeaderField, 0, HANDSHAKE_HEADER.length());
        String handshakeHeaderString = new String(handshakeHeaderField);
        if(!HANDSHAKE_HEADER.equals(handshakeHeaderString)) {
            return false;
        }

        byte[] zeroBitsField = new byte[10];
        System.arraycopy(incomingMessage, HANDSHAKE_HEADER.length(), zeroBitsField, 0, 10);
        for(int i = 0; i < 10; i++) {
            if(zeroBitsField[i] != 0) {
                return false;
            }
        }

        byte[] peerIdField = new byte[4];
        System.arraycopy(incomingMessage, HANDSHAKE_HEADER.length() + 10, peerIdField, 0, 4);
        for(int i = 0; i < 4; i++) {
            if(!Character.isDigit(peerIdField[i])) {
                return false;
            }
        }

        return true;
    }

    public static int getRemotePeerID(byte[] incomingMessage) {
        byte[] peerId = new byte[4];
        System.arraycopy(incomingMessage, HANDSHAKE_HEADER.length() + 10, peerId, 0, 4);
        return ByteBuffer.wrap(peerId).getInt();
    }
}
