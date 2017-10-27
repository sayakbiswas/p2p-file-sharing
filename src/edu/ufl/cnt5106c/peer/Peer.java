package edu.ufl.cnt5106c.peer;

import edu.ufl.cnt5106c.config.CommonConfig;

import java.net.Socket;
import java.util.*;

/**
 * Created by sayak on 10/26/17.
 */
public class Peer {
    private int id;
    private String ipAddress;
    private int portNumber;
    private boolean hasFile;
    private boolean[] availableFilePieces;
    private List<Socket> sockets;
    private Map<Integer, Peer> neighborMap;
    private CommonConfig commonConfig;
    private File file;
    private boolean isUnchoked;
    private List<Integer> interestedPeers;
    private List<Integer> unchokedPeers;
    private int optimisticallyUnchokedPeer;
    private Map<Integer, Integer> requestedPieceMap;

    public Peer(int id, String ipAddress, int portNumber, boolean hasFile) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.hasFile = hasFile;
        this.sockets = new ArrayList<>();
        this.neighborMap = new HashMap<>();
        this.commonConfig = new CommonConfig("Common.cfg");
        this.file = new File(commonConfig, hasFile);
        this.availableFilePieces = new boolean[this.file.getNumberOfPieces()];
        Arrays.fill(availableFilePieces, hasFile);
        this.isUnchoked = false;
        this.interestedPeers = new ArrayList<>();
        this.unchokedPeers = new ArrayList<>();
        this.optimisticallyUnchokedPeer = 0;
        this.requestedPieceMap = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean hasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public boolean[] getAvailableFilePieces() {
        return availableFilePieces;
    }

    public List<Socket> getSockets() {
        return sockets;
    }

    public void addSocket(Socket socket) {
        this.sockets.add(socket);
    }

    public Map<Integer, Peer> getNeighborMap() {
        return neighborMap;
    }

    public void setNeighborMap(Map<Integer, Peer> peerMap) {
        for(Map.Entry<Integer, Peer> entry : peerMap.entrySet()) {
            if(entry.getKey() != this.id) {
                this.neighborMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public CommonConfig getCommonConfig() {
        return commonConfig;
    }

    public void setCommonConfig(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isUnchoked() {
        return isUnchoked;
    }

    public void setUnchoked(boolean isUnchoked) {
        this.isUnchoked = isUnchoked;
    }

    public List<Integer> getInterestedPeers() {
        return interestedPeers;
    }

    public void addInterestedPeer(int id) {
        if(!interestedPeers.contains(id)) {
            interestedPeers.add(id);
        }
    }

    public void removeInterestedPeer(int id){
        if(interestedPeers.contains(id)) {
            interestedPeers.remove(id);
        }
    }

    public List<Integer> getUnchokedPeers() {
        return unchokedPeers;
    }

    public void unchokePeer(int id) {
        if(!unchokedPeers.contains(id)) {
            unchokedPeers.add(id);
        }
    }

    public void removeUnchokedPeer(int id) {
        if(unchokedPeers.contains(id)) {
            unchokedPeers.remove(id);
        }
    }

    public int getOptimisticallyUnchokedPeer() {
        return optimisticallyUnchokedPeer;
    }

    public void setOptimisticallyUnchokedPeer(int optimisticallyUnchokedPeer) {
        this.optimisticallyUnchokedPeer = optimisticallyUnchokedPeer;
    }

    public Map<Integer, Integer> getRequestedPieceMap() {
        return requestedPieceMap;
    }

    public void requestPiece(int requestedPieceIndex, int requestedPeerId) {
        requestedPieceMap.put(requestedPieceIndex, requestedPeerId);
    }

    public void removeRequestedPiece(int requestedPieceIndex) {
        requestedPieceMap.remove(requestedPieceIndex);
    }
}