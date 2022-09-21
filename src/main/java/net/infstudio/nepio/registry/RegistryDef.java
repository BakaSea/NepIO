package net.infstudio.nepio.registry;

public class RegistryDef<T> {

    private T definition;
    private String id;

    public RegistryDef(T definition, String id) {
        this.definition = definition;
        this.id = id;
    }

    public T get() {
        return definition;
    }

    public String getId() {
        return id;
    }

}
