package net.infstudio.nepio.network.api.automation;

public interface IInsertable<T> extends IMovable<T> {

    Mode getMode();

    enum Mode {
        RANDOM, SPLIT, FORCE_SPLIT, ROUND_ROBIN
    }

}
