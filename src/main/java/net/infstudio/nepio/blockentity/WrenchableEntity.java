package net.infstudio.nepio.blockentity;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public interface WrenchableEntity {

    ActionResult useWrench(ItemUsageContext context);

}
