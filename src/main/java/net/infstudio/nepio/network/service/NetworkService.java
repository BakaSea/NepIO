package net.infstudio.nepio.network.service;

import com.mojang.logging.LogUtils;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.network.NNetwork;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.util.Trie01;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manage all the network.
 */
public class NetworkService {

    private static final Logger LOGGER = LogUtils.getLogger();

    private Trie01 networkID, nodeID;
    private Set<NNetwork> networks;

    private final List<HandlerService<?>> services = new ArrayList<>();

    public static final NetworkService INSTANCE = new NetworkService();

    public void onServerStarting() {
        networkID = new Trie01();
        nodeID = new Trie01();
        networks = new HashSet<>();
        services.forEach(HandlerService::onServerStarting);
    }

    public void onServerStopped() {
        services.forEach(HandlerService::onServerStopped);
        networkID.clear();
        nodeID.clear();
        networks.clear();
    }

    public NNetwork createNetwork() {
        NNetwork network = new NNetwork(networkID.mexAndInsert());
        networks.add(network);
        services.forEach(service -> service.createNetwork(network));
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
        services.forEach(service -> service.removeNetwork(network));
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
        services.forEach(service -> service.addNodeToNetwork(node, network));
    }

    public void removeNodeInNetwork(NNetworkNode node, NNetwork network) {
        services.forEach(service -> service.removeNodeInNetwork(node, network));
        network.removeNode(node);
        node.setNetwork(null);
    }

    public Set<NNetwork> getNetworks() {
        return networks;
    }

    private void mergeInPrior(NNetwork n1, NNetwork n2) {
        for (NNetworkNode node : n2.getNodes()) {
            node.setNetwork(n1);
            services.forEach(service -> {
                service.removeNodeInNetwork(node, n2);
                service.addNodeToNetwork(node, n1);
            });
        }
        n1.getNodes().addAll(n2.getNodes());
        n2.getNodes().clear();
    }

    public void mergeNetwork(NNetwork n1, NNetwork n2) {
        if (n1.getNodes().size() >= n2.getNodes().size()) {
            mergeInPrior(n1, n2);
            removeNetwork(n2);
        } else {
            mergeInPrior(n2, n1);
            removeNetwork(n1);
        }
    }

    public void registerHandlerService(HandlerService<?> service) {
        services.add(service);
    }

    public void tick() {
        for (HandlerService<?> service : services) {
            service.tick();
        }
    }

}
