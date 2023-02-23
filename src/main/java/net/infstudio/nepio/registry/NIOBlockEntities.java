package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.infstudio.nepio.blockentity.TankEntity;
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

    public static final BlockEntityType<TankEntity> TANK_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(NepIO.MODID, "tank_entity"),
        FabricBlockEntityTypeBuilder.create(TankEntity::new,
            NIOBlocks.TANK_1.get(), NIOBlocks.TANK_2.get(), NIOBlocks.TANK_3.get(), NIOBlocks.TANK_4.get(), NIOBlocks.TANK_5.get()).build()
    );

    public void onInitialize() {
        FluidStorage.SIDED.registerForBlockEntity((tank, direction) -> tank.getStorage(), TANK_ENTITY);
    }

}
