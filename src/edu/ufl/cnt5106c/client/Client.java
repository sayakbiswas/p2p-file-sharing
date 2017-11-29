package edu.ufl.cnt5106c.client;

import edu.ufl.cnt5106c.messages.HandshakeMessage;
import edu.ufl.cnt5106c.peer.MessageController;
import edu.ufl.cnt5106c.peer.Peer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by sayak on 10/27/17.
 */
public class Client {
    private Peer peer;
    private Peer remotePeer;
    Socket socket;

    public Client(Peer peer, Peer remotePeer) {
        this.peer = peer;
        this.remotePeer = peer;
    }

    public void connect() {
        try {
            socket = new Socket(remotePeer.getIpAddress(), remotePeer.getPortNumber());
            peer.addSocket(socket);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            MessageController messageController = new MessageController(socket, peer, outputStream);
            Thread thread = new Thread(messageController);
            thread.start();
            byte[] message = HandshakeMessage.getMessage(peer.getId());
            peer.send(message);
        } catch(IOException ioException) {
            System.out.println("IOException while connecting to server " + remotePeer.getId());
            ioException.printStackTrace();
        }
    }
}
