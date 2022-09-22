package net.infstudio.nepio.network.api.automation;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.infstudio.nepio.network.api.IComponent;

import java.util.function.Predicate;

public interface IMovable<T> extends IComponent {

    boolean isEnabled();

    int getPriority();

    Predicate<T> getFilter();

    Storage<T> getStorage();

    Class<T> get();

}
