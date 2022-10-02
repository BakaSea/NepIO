package net.infstudio.nepio.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class Wrench extends BaseItem {

    public Wrench() {
        super(new FabricItemSettings());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient() && world.getBlockEntity(context.getBlockPos()) instanceof NIOBaseBlockEntity blockEntity) {
            return blockEntity.useWrench(context);
        }
        return ActionResult.SUCCESS;
    }

}
