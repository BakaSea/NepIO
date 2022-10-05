package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.INetworkEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * Part entity which is attached on the nep cable etc.
 * Similar with {@link BlockEntity}.
 * Use with {@link PartBaseItem}.
 */
public abstract class PartBaseEntity implements INetworkEntity {

    protected PartBaseItem item;
    protected NIOBaseBlockEntity blockEntity;
    protected Direction direction;

    public PartBaseEntity(PartBaseItem item, NIOBaseBlockEntity blockEntity, Direction direction) {
        this.item = item;
        this.blockEntity = blockEntity;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
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

    @Override
    public List<IComponent> getComponents() {
        return Collections.emptyList();
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

    public void dropItems(World world, BlockPos pos) {

    }

}
