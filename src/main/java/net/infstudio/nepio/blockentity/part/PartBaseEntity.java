package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.INetworkEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class PartBaseEntity implements INetworkEntity {

    protected PartBaseItem item;
    protected NIOBaseBlockEntity blockEntity;
    protected Direction direction;

    public PartBaseEntity(PartBaseItem item, NIOBaseBlockEntity blockEntity, Direction direction) {
        this.item = item;
        this.blockEntity = blockEntity;
        this.direction = direction;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public World getWorld() {
        return blockEntity.getWorld();
    }

    public BlockPos getPos() {
        return blockEntity.getPos();
    }

    @Override
    public void setNetworkNode(NNetworkNode networkNode) {

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

    public void onRemove() {

    }

}
