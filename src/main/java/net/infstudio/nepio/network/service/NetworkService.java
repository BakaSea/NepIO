package net.infstudio.nepio.network.service;

import com.mojang.logging.LogUtils;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.network.NNetwork;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.ComponentVisitor;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.util.Trie01;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Manage all the network.
 */
public class NetworkService {

    private static final Logger LOGGER = LogUtils.getLogger();

    private Trie01 networkID, nodeID;
    private Set<NNetwork> networks;

    public static final NetworkService INSTANCE = new NetworkService();

    public void onServerStarting() {
        networkID = new Trie01();
        nodeID = new Trie01();
        networks = new HashSet<>();
    }

    public void onServerStopped() {
        networkID.clear();
        nodeID.clear();
        networks.clear();
    }

    public NNetwork createNetwork() {
        NNetwork network = new NNetwork(networkID.mexAndInsert());
        networks.add(network);
        LOGGER.info("Create network#{}", network.getId());
        return network;
    }

    public NNetworkNode createNetworkNode(NIOBaseBlockEntity blockEntity) {
        NNetworkNode node = new NNetworkNode(nodeID.mexAndInsert(), blockEntity);
        blockEntity.setNetworkNode(node);
        LOGGER.info("Create network node#{}", node.getId());
        return node;
    }

    public void removeNetwork(NNetwork network) {
        LOGGER.info("Remove network#{}", network.getId());
        networkID.remove(network.getId());
        networks.remove(network);
        network.destroy();
    }

    public void removeNetworkNode(NNetworkNode node) {
        LOGGER.info("Remove network node#{}", node.getId());
        if (node.getNetwork() != null) removeNodeInNetwork(node, node.getNetwork());
        nodeID.remove(node.getId());
    }

    public void addNodeToNetwork(NNetworkNode node, NNetwork network) {
        network.addNode(node);
        node.setNetwork(network);
    }

    public void removeNodeInNetwork(NNetworkNode node, NNetwork network) {
        network.removeNode(node);
        node.setNetwork(null);
    }

    public Set<NNetwork> getNetworks() {
        return networks;
    }

    public void mergeNetwork(NNetwork n1, NNetwork n2) {
        NNetwork n3 = NNetwork.merge(n1, n2);
        if (n3 == n1) removeNetwork(n2);
        else removeNetwork(n1);
    }

    public static <T> void visitNetwork(NNetwork network, ComponentVisitor<T> visitor) {
        for (NNetworkNode node : network.getNodes()) {
            for (IComponent component : node.getComponents()) {
                component.accept(visitor);
            }
        }
    }

}
