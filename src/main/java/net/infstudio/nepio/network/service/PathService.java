package net.infstudio.nepio.network.service;

import com.mojang.logging.LogUtils;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.network.NNetwork;
import net.infstudio.nepio.network.NNetworkNode;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;

public class PathService {

    private static final Logger LOGGER = LogUtils.getLogger();

    private Queue<NNetworkNode> queue;

    public static final PathService INSTANCE = new PathService();

    public void onServerStarting() {
        this.queue = new ArrayDeque<>();
    }

    public void onNIOBaseBlockEntityLoad(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof NIOBaseBlockEntity nioBE) {
            NNetworkNode node = NetworkService.INSTANCE.createNetworkNode(nioBE);
            queue.add(node);
        }
    }

    public void onNIOBaseBlockEntityUnload(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof NIOBaseBlockEntity nioBE) {
            NNetwork network = nioBE.getNetworkNode().getNetwork();
            NetworkService.INSTANCE.removeNetworkNode(nioBE.getNetworkNode());
            if (network != null) {
                queue.addAll(network.getNodes());
                NetworkService.INSTANCE.removeNetwork(network);
            }
        }
    }

    public void updateNetwork(NNetworkNode node) {
        NNetwork network = node.getNetwork();
        if (network != null) {
            queue.addAll(network.getNodes());
            NetworkService.INSTANCE.removeNetwork(network);
        }
    }

    public void pathFinding() {
        boolean flag = false;
        while (!queue.isEmpty()) {
            flag = true;
            NNetworkNode cur = queue.remove();
            World world = cur.getBlockEntity().getWorld();
            if (!world.isPosLoaded(cur.getPos().getX(), cur.getPos().getZ())) continue;
            if (cur.getNetwork() == null) {
                NNetwork network = NetworkService.INSTANCE.createNetwork();
                NetworkService.INSTANCE.addNodeToNetwork(cur, network);
            }
            NNetwork curNetwork = cur.getNetwork();
            for (BlockPos pos : cur.getPossibleConnection()) {
                if (world.isPosLoaded(pos.getX(), pos.getZ()) && world.getBlockEntity(pos) instanceof NIOBaseBlockEntity nioBE) {
                    if (nioBE.canConnect(cur.getPos())) {
                        NNetworkNode next = nioBE.getNetworkNode();
                        NNetwork nextNetwork = next.getNetwork();
                        if (nextNetwork == null) {
                            NetworkService.INSTANCE.addNodeToNetwork(next, curNetwork);
                            queue.add(next);
                        } else if (curNetwork != nextNetwork) {
                            NetworkService.INSTANCE.mergeNetwork(curNetwork, nextNetwork);
                            curNetwork = cur.getNetwork();
                        }
                    }
                }
            }
        }

        if (flag) {
            LOGGER.info("Path finding finish");
            for (NNetwork network : NetworkService.INSTANCE.getNetworks()) {
                LOGGER.info("Network#{}:", network.getId());
                for (NNetworkNode node : network.getNodes()) {
                    BlockPos pos = node.getPos();
                    LOGGER.info("Node#{}: {}, {}, {}", node.getId(), pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }

    }

}
