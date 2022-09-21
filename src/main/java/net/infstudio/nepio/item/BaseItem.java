package net.infstudio.nepio.item;

import net.infstudio.nepio.NepIO;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item {

    public BaseItem(Settings settings) {
        super(settings.group(NepIO.NEP_GROUP));
    }

}
