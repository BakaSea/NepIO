package net.infstudio.nepio.network.api;

import net.infstudio.nepio.network.api.automation.IExtractable;
import net.infstudio.nepio.network.api.automation.IInsertable;

public interface ComponentVisitor<T> {

    default T visit(IInsertable<?> insertable) {
        return visitDefault(insertable);
    }

    default T visit(IExtractable<?> extractable) {
        return visitDefault(extractable);
    }

    default T visit(IComponent component) {
        return visitDefault(component);
    }

    default T visitDefault(IComponent component) {
        return null;
    }

}
