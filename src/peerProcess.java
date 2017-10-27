import edu.ufl.cnt5106c.config.PeerInfoConfig;
import edu.ufl.cnt5106c.peer.Peer;

import java.util.List;

/**
 * Created by sayak on 10/27/17.
 */
public class peerProcess {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java peerProcess <peerId>");
            return;
        }
        int peerId = Integer.parseInt(args[0]);
        PeerInfoConfig peerInfoConfig = new PeerInfoConfig("PeerInfo.cfg");
        Peer peer = peerInfoConfig.getPeer(peerId);
        //TODO: Start connection listener
        //TODO: Manage choking
        //TODO: Manage optimistic unchoking

        List<Peer> peers = peerInfoConfig.getAllPeers();

        for(Peer neighbor : peers) {
            //TODO: open client connections with neighbors
        }
    }
}
