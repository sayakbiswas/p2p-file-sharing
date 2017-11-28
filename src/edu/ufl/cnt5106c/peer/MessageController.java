package edu.ufl.cnt5106c.peer;

import edu.ufl.cnt5106c.messages.*;

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
                    //Ankit
                    else if(incomingMessage[4] == 2)
                    {
                        //received interested msg from peer
                        peer.addInterestedPeer(remotePeerId);
                    }
                    else if(incomingMessage[4] == 3)
                    {
                        //recived not interested msg from peer
                        peer.removeInterestedPeer(remotePeerId);
                    }
                    else if(incomingMessage[4] == 4)
                    {
                        int index = HaveMessage.getIndex(incomingMessage);//piece index
                        peer.updateneighbourPiece(remotePeerId,index);
                        if(peer.getAvailableFilePieces()[index])
                        {
                            byte[] msg_notInterested = NotInterestedMessage.getMessage();
                            peer.send(msg_notInterested); // Send not interested message to remote peer
                        }
                        else
                        {
                            byte[] msg_Intereted = InterestedMessage.getMessage();
                            peer.send(msg_Intereted);//Send Interested message to remote peer
                        }
                    }
                    else if(incomingMessage[4] == 5)
                    {
                        boolean remotepeermsgpiece[] = BitfieldMessage.getBitFieldPieces(incomingMessage);
                        for(int len =0;len<remotepeermsgpiece.length;len++)
                        {
                            if(remotepeermsgpiece[len] == true)
                            {
                                peer.updateneighbourPiece(remotePeerId,len);
                            }
                        }
                        if( peer.getPieceMissing(remotePeerId)!= -1)
                        {
                            byte message[] = InterestedMessage.getMessage(); //interested piece
                            peer.send(message);
                        }
                        else
                        {
                            byte message[] = NotInterestedMessage.getMessage(); // not interested piece
                            peer.send(message);
                        }
                    }
                    else if(incomingMessage[4] == 6)
                    {
                        int index = RequestMessage.getIndex(incomingMessage);
                      //

                    }
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println("IOException in message controller of peer " + peer.getId());
            ioException.printStackTrace();
        } catch(ClassNotFoundException classNotFoundException) {
            System.out.println("ClassNotFoundException in message controller of peer " + peer.getId());
            classNotFoundException.printStackTrace();
        }
    }
}
