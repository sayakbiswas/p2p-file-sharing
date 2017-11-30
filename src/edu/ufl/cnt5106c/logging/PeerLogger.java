package edu.ufl.cnt5106c.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by sayak on 10/26/17.
 */
//Added by Ankit
public class PeerLogger
{
    private static Logger LOGGER;
    private static int Id;
    private static FileHandler logHandler;
    private static SimpleFormatter logformatter;

    public PeerLogger(int peerId) throws IOException
    {
        //Constructor
        Id = peerId;
        LOGGER = Logger.getLogger("PeerLogger"+PeerLogger.Id);
        LOGGER.setLevel(Level.INFO);
        logHandler  = new FileHandler("log_peer_"+peerId+".log");
        logformatter = new SimpleFormatter();
        logHandler.setFormatter(logformatter);
        LOGGER.addHandler(logHandler);
    }

    public static void logTCPCreateConnection(int remotePeerId) {
        LOGGER.info(":Peer " + Id + " makes a connection to Peer " + remotePeerId + "\n");
    }

    public static void logTCPReceiveConnection(int remotePeerId) {
        LOGGER.info(":Peer " + Id + " is connected from Peer " + remotePeerId + "\n");
    }

    public static void logChangeOfPreferredNeighbors(ArrayList<Integer> preferredNeighbors) {
        StringBuffer textlog = new StringBuffer();
        textlog.append(":Peer " + Id + " has the preferred neighbores ");

        for (int i = 0; i < preferredNeighbors.size(); i++)
        {
            if (i != (preferredNeighbors.size() - 1))
            {
                textlog.append(preferredNeighbors.get(i) + ", ");
            }
            else
            {
                textlog.append(preferredNeighbors.get(i) + "\n");
            }
        }
        LOGGER.info(textlog.toString());
    }

    public static void logChangeOfOptimisticallyUnchokedNeighbor(int optimisticallyUnchokedNeighbor) {
        LOGGER.info(": Peer " + Id + " has the optimistically unchoked neighbor " + optimisticallyUnchokedNeighbor + "\n");
    }

    public static void logUnchoking(int remotePeerId) {
        LOGGER.info(": Peer " + Id + " is unchoked by " + remotePeerId + "\n");
    }


    public static void logChoking(int remotePeerId) {
        LOGGER.info(": Peer " + Id + " is choked by " + remotePeerId + "\n");
    }

    public static void logReceiveHaveMessage(int remotePeerId, int pieceIndex) {
        LOGGER.info(": Peer " + Id + " received the ‘have’message from " + remotePeerId + " for the piece " + pieceIndex + "\n");
    }

    public static void logReceiveInterestedMessage(int remotePeerId) {
        LOGGER.info(": Peer " + Id + " received the ‘interested’message from " + remotePeerId + "\n");
    }

    public static void logReceiveNotInterestedMessage(int remotePeerId) {
        LOGGER.info(": Peer " + Id + " received the ‘not interested’message from " + remotePeerId + "\n");
    }

    public static void logDownloadPiece(int remotePeerId, int pieceIndex, int numberOfPieces) {
        LOGGER.info(": Peer " + Id + " has downloaded the piece " + pieceIndex +
                " from " + remotePeerId + "." +
                "Now the number of pieces it has is "+numberOfPieces+"\n");
    }

    public static void logDownloadComplete() {
        LOGGER.info(": Peer " + Id + " has downloaded the complete file \n");
    }
}
