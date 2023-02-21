package net.infstudio.nepio.network;

import net.infstudio.nepio.network.service.HandlerService;

import java.util.HashSet;
import java.util.Set;

public class NNetwork {

    private int id;
    private Set<NNetworkNode> nodes;

    public NNetwork(int id) {
        this.id = id;
        this.nodes = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public Set<NNetworkNode> getNodes() {
        return nodes;
    }

    public void addNode(NNetworkNode node) {
        nodes.add(node);
    }

    public void removeNode(NNetworkNode node) {
        nodes.remove(node);
    }

    public void destroy() {
        for (NNetworkNode node : nodes) {
            node.setNetwork(null);
        }
        nodes.clear();
    }

}
