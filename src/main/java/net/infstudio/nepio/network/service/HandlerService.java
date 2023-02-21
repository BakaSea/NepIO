package net.infstudio.nepio.network.service;

import net.infstudio.nepio.network.NNetwork;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.IComponent;

import java.util.HashMap;
import java.util.Map;

public abstract class HandlerService<P extends HandlerService.NetworkPayload> {

    protected Map<NNetwork, P> payloads;

    public void tick() {

    }

    public void onServerStarting() {
        payloads = new HashMap<>();
    }

    public void onServerStopped() {
        payloads.clear();
    }

    protected abstract P createPayload();

    public void createNetwork(NNetwork network) {
        payloads.put(network, createPayload());
    }

    public void removeNetwork(NNetwork network) {
        payloads.remove(network);
    }

    public void addNodeToNetwork(NNetworkNode node, NNetwork network) {
        P payload = payloads.get(network);
        node.getComponents().forEach(payload::addComponent);
    }

    public void removeNodeInNetwork(NNetworkNode node, NNetwork network) {
        P payload = payloads.get(network);
        node.getComponents().forEach(payload::removeComponent);
    }

    protected abstract class NetworkPayload {
        public int tick;
        public abstract void addComponent(IComponent component);
        public abstract void removeComponent(IComponent component);
    }

}

