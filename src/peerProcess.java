import edu.ufl.cnt5106c.client.Client;
import edu.ufl.cnt5106c.config.PeerInfoConfig;
import edu.ufl.cnt5106c.messages.ChokeMessage;
import edu.ufl.cnt5106c.peer.Peer;
import edu.ufl.cnt5106c.server.Server;

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

        //Start connection listener
        Server server = new Server(peer);
        Thread serverThread = new Thread(server);
        serverThread.start();

        //Manage choking/unchoking
        peer.startPreferredNeighborSelection();

        //Manage optimistic unchoking
        peer.startOptimisticallyUnchokingPeer();

        List<Peer> peers = peerInfoConfig.getAllPeers();

        for(Peer neighbor : peers) {
            if(neighbor.getId() != peerId) {
                Client client = new Client(peer, neighbor);
                client.connect();
            }
        }
    }
}
