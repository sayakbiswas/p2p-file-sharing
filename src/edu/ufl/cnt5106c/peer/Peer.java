package edu.ufl.cnt5106c.peer;

import edu.ufl.cnt5106c.config.CommonConfig;
import edu.ufl.cnt5106c.messages.ChokeMessage;
import edu.ufl.cnt5106c.messages.UnchokeMessage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private int countPieces;
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
    private ObjectOutputStream outputStream;
    private long timerToSelectPreferredNeighbors;
    private long timerToOptimisticallyUnchokeNeighbor;
    private long timeUnchoked;
    private long bytesDownloadedAfterUnchoking;
    private double downloadRate;

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
        this.timerToSelectPreferredNeighbors = (long)commonConfig.getUnchokingInterval() * 1000;
        this.timerToOptimisticallyUnchokeNeighbor = (long)commonConfig.getOptimisticUnchokingInterval() * 1000;
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

    public void setFilePieceAvailabilityAtIndex(boolean availability, int index) {
        availableFilePieces[index] = true;
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

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    //Added by Ankit
    public void updateneighbourPiece(int id, int index){
        if(index < countPieces)
            neighborMap.get(id).availableFilePieces[index]=true;
    }

    public int getPieceMissing(int remotePeerId) //Added by Ankit
    {
        boolean[] currPieces = this.getAvailableFilePieces();
        Peer peerMap = this.getNeighborMap().get(remotePeerId);
        int ind = 0;
        boolean[] available = peerMap.availableFilePieces;
        List<Integer> miss = new ArrayList<>();
        for(boolean cp:currPieces)
        {
            for(boolean rp:available)
            {
                if(!(cp) && !requestedPieceMap.containsKey(ind) && rp )
                {
                    miss.add(ind);
                }
            }
            ind++;

        }
        if(miss.size() == 0)
        {
            return -1;
        }
        Random r = new Random();
        int rInt = r.nextInt(miss.size());
        return miss.get(rInt);
    }

    public long getTimeUnchoked() {
        return timeUnchoked;
    }

    public long getBytesDownloadedAfterUnchoking() {
        return bytesDownloadedAfterUnchoking;
    }

    public double getDownloadRate() {
        return downloadRate;
    }

    public void setDownloadRate(double downloadRate) {
        this.downloadRate = downloadRate;
    }

    public void startPreferredNeighborSelection() {
        Peer peer = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int interestedPeerId : peer.interestedPeers) {
                        Peer interestedPeer = peer.getNeighborMap().get(interestedPeerId);
                        long currentTime = System.currentTimeMillis();
                        interestedPeer.setDownloadRate(
                                (double)interestedPeer.getBytesDownloadedAfterUnchoking()
                                        / (double)(currentTime - interestedPeer.getTimeUnchoked()));

                    }
                    if(!peer.interestedPeers.isEmpty()) {
                        if(peer.hasFile) {
                            Collections.shuffle(peer.interestedPeers);
                            peer.chokeOrUnchokePeers(peer.interestedPeers);
                        } else {
                            Collections.sort(interestedPeers, new Comparator<Integer>() {
                                @Override
                                public int compare(Integer peer1, Integer peer2) {
                                    double downloadRate1 = peer.getNeighborMap().get(peer1).getDownloadRate();
                                    double downloadRate2 = peer.getNeighborMap().get(peer2).getDownloadRate();
                                    return (int)(downloadRate2 - downloadRate1);
                                }
                            });
                            peer.chokeOrUnchokePeers(peer.interestedPeers);
                        }
                    }
                    try {
                        Thread.sleep(peer.timerToSelectPreferredNeighbors);
                    } catch (InterruptedException interruptedException) {
                        System.out.println("Thread to reselect preferred neighbors interrupted while trying to sleep.");
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void chokeOrUnchokePeers(List<Integer> interestedPeers) {
        List<Integer> preferredNeighbors = interestedPeers.subList(0, getCommonConfig().getNumberOfPreferredNeighbors());
        for(int unchokedPeerId : getUnchokedPeers()) {
            if(getOptimisticallyUnchokedPeer() != unchokedPeerId && !preferredNeighbors.contains(unchokedPeerId)) {
                byte[] message = ChokeMessage.getMessage();
                getNeighborMap().get(unchokedPeerId).send(message);
                removeUnchokedPeer(unchokedPeerId);
            }
        }

        for(int preferredNeighbor : preferredNeighbors) {
            if(!getUnchokedPeers().contains(preferredNeighbor)) {
                byte[] message = UnchokeMessage.getMessage();
                getNeighborMap().get(preferredNeighbor).send(message);
                unchokePeer(preferredNeighbor);
            }
        }
    }

    public void startOptimisticallyUnchokingPeer() {
        Peer peer = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    peer.optimisticallyUnchokePeer(peer.getInterestedPeers());
                    try {
                        Thread.sleep(timerToOptimisticallyUnchokeNeighbor);
                    } catch (InterruptedException interruptedException) {
                        System.out.println("Thread to optimistically unchoke neighbor interrupted while trying to sleep.");
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void optimisticallyUnchokePeer(List<Integer> interestedPeers) {
        List<Integer> candidatePeers = new ArrayList<>();
        for(int interestedPeerId : interestedPeers) {
            if(!getUnchokedPeers().contains(interestedPeerId)) {
                candidatePeers.add(interestedPeerId);
            }
        }
        if(!candidatePeers.isEmpty()) {
            Collections.shuffle(candidatePeers);
            int optimisticallyUnchokedPeerId = candidatePeers.get(0);
            byte[] message = UnchokeMessage.getMessage();
            getNeighborMap().get(optimisticallyUnchokedPeerId).send(message);
            setOptimisticallyUnchokedPeer(optimisticallyUnchokedPeerId);
        }
    }

    public void resetUnchokeTimer() {
        timeUnchoked = System.currentTimeMillis();
        bytesDownloadedAfterUnchoking = 0;
    }

    public void send(byte[] message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException ioException) {
            System.out.println("IOException while sending message " + new String(message) + "from peer " + getId());
            ioException.printStackTrace();
        }
    }

    public byte[] getDataInPiece(int pieceIndex) {
        return file.getPieceAtIndex(pieceIndex).getData();
    }

    public void updateDataReceivedFromPeer(int remotePeerId, int length) {
        getNeighborMap().get(remotePeerId).bytesDownloadedAfterUnchoking += length;
    }

    public boolean hasCompleteFile() {
        for(boolean availableFilePiece : availableFilePieces) {
            if(!availableFilePiece) {
                return false;
            }
        }
        return true;
    }

    public void saveFileToDisk() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(commonConfig.getSharedFileName());
            for(Piece piece : file.getPieces()) {
                fileOutputStream.write(piece.getData());
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("FileNotFoundException while saving downloaded file to disk.");
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("IOException while writing pieces to file.");
            ioException.printStackTrace();
        } finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch(IOException ioException) {
                    System.out.println("IOException while closing file output stream");
                    ioException.printStackTrace();
                }
            }
        }
    }
}
