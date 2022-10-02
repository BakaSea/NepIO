package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.infstudio.nepio.NepIO;

public class NIOBlockEntities implements ModInitializer {

    public static final NIOBlockEntities INSTANCE = new NIOBlockEntities();

    public static final BlockEntityType<NepCableEntity> NEP_CABLE_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(NepIO.MODID, "nep_cable_entity"),
        FabricBlockEntityTypeBuilder.create(NepCableEntity::new, NIOBlocks.NEP_CABLE.get()).build()
    );

    public void onInitialize() {

    }

}
