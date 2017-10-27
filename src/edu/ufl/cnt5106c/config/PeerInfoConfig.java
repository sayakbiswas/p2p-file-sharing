package edu.ufl.cnt5106c.config;

import edu.ufl.cnt5106c.peer.Peer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sayak on 10/26/17.
 */
public class PeerInfoConfig {
    private Map<Integer, Peer> peerMap;

    public Peer getPeer(int id) {
        return peerMap.get(id);
    }

    public List<Peer> getAllPeers() {
        return (List<Peer>) peerMap.values();
    }

    public PeerInfoConfig(String peerInfoConfigFile) {
        this.peerMap = new LinkedHashMap<>();
        try {
            loadPeerInfoConfig(peerInfoConfigFile);
            for(Map.Entry<Integer, Peer> entry : peerMap.entrySet()) {
                entry.getValue().setNeighborMap(peerMap);
            }
        } catch (Exception e) {
            System.out.println("Error initializing PeerInfoConfig");
            e.printStackTrace();
        }
    }

    public void loadPeerInfoConfig(String peerInfoConfigFile) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(peerInfoConfigFile));
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Could not find config file " + peerInfoConfigFile);
            fileNotFoundException.printStackTrace();
        }
        if(bufferedReader != null) {
            String line = "";
            try {
                while((line = bufferedReader.readLine()) != null) {
                    String[] tokens = line.trim().split("\\s+");
                    int peerId = Integer.parseInt(tokens[0]);
                    String peerIpAddress = tokens[1];
                    int peerPortNumber = Integer.parseInt(tokens[2]);
                    boolean peerHasFile = Integer.parseInt(tokens[3]) == 1;
                    peerMap.put(peerId, new Peer(peerId, peerIpAddress, peerPortNumber, peerHasFile));
                }
            } catch (IOException ioException) {
                System.out.println("IOException while reading config file " + peerInfoConfigFile);
                ioException.printStackTrace();
            }
        }
    }
}
