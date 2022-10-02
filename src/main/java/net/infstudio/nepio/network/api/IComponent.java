package net.infstudio.nepio.network.api;

import net.minecraft.nbt.NbtCompound;

public interface IComponent {

    default void readNbt(NbtCompound nbt) {

    }

    default void writeNbt(NbtCompound nbt) {

    }

    default <T> T accept(ComponentVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
