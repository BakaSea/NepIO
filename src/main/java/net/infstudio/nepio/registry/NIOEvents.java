package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.infstudio.nepio.block.BarrelBlock;
import net.infstudio.nepio.item.Wrench;
import net.infstudio.nepio.network.NetworkEventHandler;

public class NIOEvents implements ModInitializer {

    public static final NIOEvents INSTANCE = new NIOEvents();

    @Override
    public void onInitialize() {
        NetworkEventHandler.INSTANCE.onInitialize();
        UseBlockCallback.EVENT.register(Wrench::interact);
        UseBlockCallback.EVENT.register(BarrelBlock::insert);
        AttackBlockCallback.EVENT.register(BarrelBlock::extract);
    }

}
