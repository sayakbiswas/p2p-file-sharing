package edu.ufl.cnt5106c.server;

import edu.ufl.cnt5106c.peer.Peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sayak on 10/27/17.
 */
public class Server implements Runnable {
    private Peer peer;

    public Server(Peer peer) {
        this.peer = peer;
    }

    public void run() {
        int portNumber = peer.getPortNumber();
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(portNumber);
            while(true) {
                Socket socket = listener.accept();
                peer.addSocket(socket);
                //TODO: Create a class for sending and receiving messages
            }
        } catch(IOException ioException) {
            System.out.println("IOException while opening socket at port number " + portNumber + " for peer " + peer.getId());
            ioException.printStackTrace();
        } finally {
            if(listener != null) {
                try {
                    listener.close();
                } catch (IOException ioException1) {
                    System.out.println("IOException while closing listener socket at " + portNumber + " for peer " + peer.getId());
                    ioException1.printStackTrace();
                }
            }
        }
    }
}
