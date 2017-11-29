package edu.ufl.cnt5106c.server;

import edu.ufl.cnt5106c.messages.HandshakeMessage;
import edu.ufl.cnt5106c.peer.MessageController;
import edu.ufl.cnt5106c.peer.Peer;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                MessageController messageController = new MessageController(socket, peer, outputStream);
                Thread thread = new Thread(messageController);
                thread.start();
                byte[] message = HandshakeMessage.getMessage(peer.getId());
                peer.send(message);
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
