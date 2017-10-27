package edu.ufl.cnt5106c.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sayak on 10/26/17.
 */
public class PeerLogger {
    private static Logger LOGGER;
    private static int peerId;
    public static void setup(int peerId) throws IOException { //TODO: Handle this
        PeerLogger.peerId = peerId;
        LOGGER = Logger.getLogger("PeerLogger");
        LOGGER.setLevel(Level.INFO);
        LOGGER.addHandler(new FileHandler("log_peer_" + peerId + ".log"));
    }

    public static void logTCPCreateConnection(int remotePeerId) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " makes a connection to Peer " + remotePeerId;
        LOGGER.log(Level.INFO, message);
    }

    public static void logTCPReceiveConnection(int remotePeerId) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " is connected from Peer " + remotePeerId;
        LOGGER.log(Level.INFO, message);
    }

    public static void logChangeOfPreferredNeighbors(String preferredNeighbors) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " has the preferred neighbors " + preferredNeighbors;
        LOGGER.log(Level.INFO, message);
    }

    public static void logChangeOfOptimisticallyUnchokedNeighbor(int optimisticallyUnchokedNeighbor) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " has the optimistically unchoked neighbor " +
                optimisticallyUnchokedNeighbor;
        LOGGER.log(Level.INFO, message);
    }

    public static void logUnchoking(int remotePeerId) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " is unchoked by " + remotePeerId;
        LOGGER.log(Level.INFO, message);
    }

    public static void logChoking(int remotePeerId) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " is choked by " + remotePeerId;
        LOGGER.log(Level.INFO, message);
    }

    public static void logReceiveHaveMessage(int remotePeerId, int pieceIndex) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " received 'have' message from " + remotePeerId +
                " for the piece " + pieceIndex;
        LOGGER.log(Level.INFO, message);
    }

    public static void logReceiveInterestedMessage(int remotePeerId) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " received 'interested' message from " + remotePeerId;
        LOGGER.log(Level.INFO, message);
    }

    public static void logReceiveNotInterestedMessage(int remotePeerId) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " received 'not interested' message from " + remotePeerId;
        LOGGER.log(Level.INFO, message);
    }

    public static void logDownloadPiece(int remotePeerId, int pieceIndex, int numberOfPieces) {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " has downloaded the piece " + pieceIndex +
                " from " + remotePeerId + ". Now the number of pieces it has is " + numberOfPieces;
        LOGGER.log(Level.INFO, message);
    }

    public static void logDownloadComplete() {
        String message = System.currentTimeMillis() + ": Peer " + peerId + " has downloaded the complete file.";
    }
}
