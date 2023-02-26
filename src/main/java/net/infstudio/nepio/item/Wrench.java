package net.infstudio.nepio.item;

import net.infstudio.nepio.blockentity.WrenchableEntity;
import net.infstudio.nepio.registry.NIOItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class Wrench extends BaseItem {

    public Wrench() {
        super(NIOItems.getDefaultSettings());
    }

    public static ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (player.getStackInHand(hand).getItem() == NIOItems.WRENCH.get()
            && !world.isClient() && world.getBlockEntity(hitResult.getBlockPos()) instanceof WrenchableEntity entity) {
            return entity.useWrench(new ItemUsageContext(player, hand, hitResult));
        }
        return ActionResult.PASS;
    }

}
