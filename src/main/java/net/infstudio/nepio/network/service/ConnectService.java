package net.infstudio.nepio.network.service;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.infstudio.nepio.blockentity.NepCableEntity;

import java.util.HashSet;
import java.util.Set;

public class ConnectService {

    public static final ConnectService INSTANCE = new ConnectService();

    private Set<NepCableEntity> toUpdate;

    public void onNepCableEntityLoad(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof NepCableEntity entity) {
            toUpdate.add(entity);
        }
    }

    public void onNepCableEntityUnload(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof NepCableEntity entity) {
            toUpdate.remove(entity);
            for (Direction direction : Direction.values()) {
                BlockPos pos = blockEntity.getPos().offset(direction);
                if (world.isPosLoaded(pos.getX(), pos.getZ()) && world.getBlockEntity(pos) instanceof NepCableEntity neighbor) {
                    toUpdate.add(neighbor);
                }
            }
        }
    }

    public void updateConnection(NepCableEntity entity) {
        World world = entity.getWorld();
        toUpdate.add(entity);
        for (Direction direction : Direction.values()) {
            BlockPos pos = entity.getPos().offset(direction);
            if (world.isPosLoaded(pos.getX(), pos.getZ()) && world.getBlockEntity(pos) instanceof NepCableEntity neighbor) {
                toUpdate.add(neighbor);
            }
        }
    }

    public void onServerStarting() {
        toUpdate = new HashSet<>();
    }

    public void connectUpdating() {
        for (NepCableEntity entity : toUpdate) {
            entity.updateConnection();
            World world = entity.getWorld();
            for (Direction direction : Direction.values()) {
                BlockPos pos = entity.getPos().offset(direction);
                if (world.isPosLoaded(pos.getX(), pos.getZ()) && world.getBlockEntity(pos) instanceof NepCableEntity neighbor) {
                    neighbor.updateConnection();
                }
            }
        }
        toUpdate.clear();
    }

}
