package net.infstudio.nepio.network;

import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.INetworkEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class NNetworkNode {

    private int id;
    private NNetwork network;
    private INetworkEntity entity;

    public NNetworkNode(int id, INetworkEntity entity) {
        this.id = id;
        this.entity = entity;
        this.network = null;
    }

    public int getId() {
        return id;
    }

    public NNetwork getNetwork() {
        return network;
    }

    public void setNetwork(NNetwork network) {
        this.network = network;
    }

    public BlockPos getPos() {
        return getBlockEntity().getPos();
    }

    public BlockEntity getBlockEntity() {
        return entity.getBlockEntity();
    }

    public List<BlockPos> getPossibleConnection() {
        return entity.getPossibleConnection();
    }

    public List<IComponent> getComponents() {
        return entity.getComponents();
    }

}
