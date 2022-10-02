package net.infstudio.nepio.network.api.automation;

import net.infstudio.nepio.network.api.ComponentVisitor;

public interface IExtractable<T> extends IMovable<T> {

    @Override
    default <T> T accept(ComponentVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
