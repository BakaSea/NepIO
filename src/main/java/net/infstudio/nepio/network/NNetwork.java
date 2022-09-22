package net.infstudio.nepio.network;

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

    private static NNetwork mergeInPrior(NNetwork n1, NNetwork n2) {
        for (NNetworkNode node : n2.nodes) {
            node.setNetwork(n1);
        }
        n1.nodes.addAll(n2.nodes);
        n2.nodes.clear();
        return n1;
    }

    public static NNetwork merge(NNetwork n1, NNetwork n2) {
        if (n1.nodes.size() >= n2.nodes.size()) return mergeInPrior(n1, n2);
        else return mergeInPrior(n2, n1);
    }

    public void destroy() {
        for (NNetworkNode node : nodes) {
            node.setNetwork(null);
        }
        nodes.clear();
    }

}
