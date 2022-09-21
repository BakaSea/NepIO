package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.INetworkEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class PartBaseEntity implements INetworkEntity {

    protected PartBaseItem item;
    protected NIOBaseBlockEntity blockEntity;

    public PartBaseEntity(PartBaseItem item, NIOBaseBlockEntity blockEntity) {
        this.item = item;
        this.blockEntity = blockEntity;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public void setNetworkNode(NNetworkNode networkNode) {
        //Do nothing
    }

    @Override
    public NNetworkNode getNetworkNode() {
        return blockEntity.getNetworkNode();
    }

    @Override
    public List<BlockPos> getPossibleConnection() {
        return null;
    }

    @Override
    public boolean canConnect(BlockPos pos) {
        return false;
    }

    public void readNbt(NbtCompound nbt) {

    }

    public void writeNbt(NbtCompound nbt) {

    }

    public PartBaseItem getItem() {
        return item;
    }

}
