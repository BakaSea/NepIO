package net.infstudio.nepio.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.infstudio.nepio.client.render.NepCableBER;
import net.infstudio.nepio.registry.NIOBlockEntities;

@Environment(EnvType.CLIENT)
public class NepIOClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(NIOBlockEntities.NEP_CABLE_ENTITY, NepCableBER::new);
    }

}
