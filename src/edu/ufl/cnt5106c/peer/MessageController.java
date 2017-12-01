package edu.ufl.cnt5106c.peer;

import edu.ufl.cnt5106c.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by sayak on 11/25/17.
 */
public class MessageController implements Runnable {
    private Socket socket;
    private Peer peer;
    private ObjectOutputStream outputStream;
    private int remotePeerId;

    public MessageController(Socket socket, Peer peer, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.peer = peer;
        this.outputStream = outputStream;
    }

    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while(true) {
                System.out.println("Reading input from peer");
                byte[] incomingMessage = (byte[])inputStream.readObject();
                System.out.println("Receiving incoming message " + new String(incomingMessage));
                if(HandshakeMessage.checkForIncomingHandshake(incomingMessage)) {
                    System.out.println("Received incoming handshake message");
                    remotePeerId = HandshakeMessage.getRemotePeerID(incomingMessage);
                    System.out.println("remotePeerId " + remotePeerId);
                    Peer remotePeer = peer.getNeighborMap().get(remotePeerId);
                    if(remotePeer != null) {
                        remotePeer.setOutputStream(outputStream);

                        boolean[] availableFilePieces = peer.getAvailableFilePieces();
                        for(int i = 0; i < availableFilePieces.length; i++) {
                            if(availableFilePieces[i]) {
                                byte[] bitfieldMessage = BitfieldMessage.getMessage(availableFilePieces);
                                System.out.println("Sending bitfield message " + new String(bitfieldMessage));
                                peer.send(outputStream, bitfieldMessage);
                                break;
                            }
                        }
                    }

                } else {
                    if(incomingMessage[4] == 0) {
                        System.out.println("Received choke message");
                        peer.setUnchoked(false);
                    } else if(incomingMessage[4] == 1) {
                        System.out.println("Received unchoke message");
                        peer.setUnchoked(true);
                        peer.getNeighborMap().get(remotePeerId).resetUnchokeTimer();
                        int missingPieceIndex = peer.getPieceMissing(remotePeerId);
                        System.out.println("Missing piece index " + missingPieceIndex);
                        if(missingPieceIndex != -1) {
                            peer.requestPiece(missingPieceIndex, remotePeerId);
                            byte[] message = RequestMessage.getMessage(missingPieceIndex);
                            System.out.println("Sending request message " + new String(message));
                            peer.send(outputStream, message);
                        }
                    }
                    //Ankit
                    else if(incomingMessage[4] == 2)
                    {
                        System.out.println("Received interested message");
                        peer.addInterestedPeer(remotePeerId);
                    }
                    else if(incomingMessage[4] == 3)
                    {
                        System.out.println("Received not interested message");
                        peer.removeInterestedPeer(remotePeerId);
                    }
                    else if(incomingMessage[4] == 4)
                    {
                        int index = HaveMessage.getIndex(incomingMessage);//piece index
                        peer.updateneighbourPiece(remotePeerId,index);
                        if(peer.getAvailableFilePieces()[index])
                        {
                            byte[] msg_notInterested = NotInterestedMessage.getMessage();
                            System.out.println("Sending not interested message " + new String(msg_notInterested));
                            peer.send(outputStream, msg_notInterested); // Send not interested message to remote peer
                        }
                        else
                        {
                            byte[] msg_Intereted = InterestedMessage.getMessage();
                            System.out.println("Sending interested message " + new String(msg_Intereted));
                            peer.send(outputStream, msg_Intereted);//Send Interested message to remote peer
                        }
                    }
                    else if(incomingMessage[4] == 5)
                    {
                        System.out.println("Received bitfield message");
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
                            System.out.println("Sending interested message " + new String(message));
                            peer.send(outputStream, message);
                        }
                        else
                        {
                            byte message[] = NotInterestedMessage.getMessage(); // not interested piece
                            System.out.println("Sending not interested message " + new String(message));
                            peer.send(outputStream, message);
                        }
                    }
                    else if(incomingMessage[4] == 6)
                    {
                        System.out.println("Received request message");
                        int pieceIndex = RequestMessage.getIndex(incomingMessage);
                        System.out.println("pieceIndex " + pieceIndex);
                        byte[] dataInPiece = peer.getDataInPiece(pieceIndex);
                        System.out.println("data in piece " + new String(dataInPiece));
                        byte[] message = PieceMessage.getMessage(pieceIndex, dataInPiece);
                        System.out.println("Sending piece message " + new String(message));
                        peer.send(outputStream, message);
                    } else if(incomingMessage[4] == 7) {
                        System.out.println("Received piece message");
                        byte[] dataInPiece = PieceMessage.retrievePieceData(incomingMessage);
                        int indexOfPiece = PieceMessage.getIndexOfPiece(incomingMessage);
                        peer.updateDataReceivedFromPeer(remotePeerId, dataInPiece.length);
                        peer.getFile().setPieceAtIndex(indexOfPiece, new Piece(dataInPiece));
                        peer.setFilePieceAvailabilityAtIndex(true, indexOfPiece);
                        boolean hasFile = peer.hasCompleteFile();
                        peer.setHasFile(hasFile);
                        if(hasFile) {
                            peer.saveFileToDisk();
                        }
                        peer.removeRequestedPiece(indexOfPiece);

                        if(!peer.hasFile() && peer.isUnchoked()) {
                            int missingPieceIndex = peer.getPieceMissing(remotePeerId);
                            if(missingPieceIndex != -1) {
                                peer.requestPiece(missingPieceIndex, remotePeerId);
                                byte[] message = RequestMessage.getMessage(missingPieceIndex);
                                peer.send(outputStream, message);
                            }
                        }

                        for(Map.Entry<Integer, Peer> entry : peer.getNeighborMap().entrySet()) {
                            if(peer.getId() != entry.getKey()) {
                                byte[] message = HaveMessage.getMessage(indexOfPiece);
                                entry.getValue().send(outputStream, message);
                            }
                        }
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
