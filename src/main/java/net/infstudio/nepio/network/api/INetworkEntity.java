package net.infstudio.nepio.network.api;

import net.infstudio.nepio.network.NNetworkNode;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface INetworkEntity {

    BlockEntity getBlockEntity();

    void setNetworkNode(NNetworkNode networkNode);

    NNetworkNode getNetworkNode();

    List<BlockPos> getPossibleConnection();

    boolean canConnect(BlockPos pos);

}
