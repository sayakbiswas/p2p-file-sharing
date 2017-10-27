package edu.ufl.cnt5106c.client;

import edu.ufl.cnt5106c.peer.Peer;

import java.io.IOException;
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
            //TODO: Create class for message transfer
        } catch(IOException ioException) {
            System.out.println("IOException while connecting to server " + remotePeer.getId());
            ioException.printStackTrace();
        } finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException1) {
                    System.out.println("IOException while closing socket with port number " + remotePeer.getPortNumber());
                    ioException1.printStackTrace();
                }
            }
        }
    }
}
