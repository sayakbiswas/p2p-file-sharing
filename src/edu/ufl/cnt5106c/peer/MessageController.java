package edu.ufl.cnt5106c.peer;

import edu.ufl.cnt5106c.messages.BitfieldMessage;
import edu.ufl.cnt5106c.messages.HandshakeMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by sayak on 11/25/17.
 */
public class MessageController implements Runnable {
    Socket socket;
    Peer peer;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    int remotePeerId;

    public MessageController(Socket socket, Peer peer, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.peer = peer;
        this.outputStream = outputStream;
    }

    public void run() {
        try {
            inputStream = (ObjectInputStream) socket.getInputStream();
            while(true) {
                byte[] incomingMessage = (byte[])inputStream.readObject();
                if(HandshakeMessage.checkForIncomingHandshake(incomingMessage)) {
                    remotePeerId = HandshakeMessage.getRemotePeerID(incomingMessage);
                    peer.getNeighborMap().get(remotePeerId).setOutputStream(outputStream);

                    boolean[] availableFilePieces = peer.getAvailableFilePieces();
                    for(int i = 0; i < availableFilePieces.length; i++) {
                        if(availableFilePieces[i]) {
                            byte[] bitfieldMessage = BitfieldMessage.getMessage(availableFilePieces);
                            peer.send(bitfieldMessage);
                            break;
                        }
                    }
                } else {
                    if(incomingMessage[4] == 0) { //Choke message
                        peer.setUnchoked(false);
                    } else if(incomingMessage[4] == 1) { //Unchoke message
                        peer.setUnchoked(true);

                    }
                }
            }
        } catch(IOException ioException) {
            System.out.println("IOException in message controller of peer " + peer.getId());
            ioException.printStackTrace();
        } catch(ClassNotFoundException classNotFoundException) {
            System.out.println("ClassNotFoundException in message controller of peer " + peer.getId());
            classNotFoundException.printStackTrace();
        }
    }
}
