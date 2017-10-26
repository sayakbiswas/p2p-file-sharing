package edu.ufl.cnt5106c.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sayak on 10/26/17.
 */
public class PeerLogger {
    public static void setup(int peerId) throws IOException { //TODO: Handle this
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        logger.addHandler(new FileHandler("log_peer_" + peerId + ".log"));
    }
}
