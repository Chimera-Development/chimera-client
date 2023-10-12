package dev.chimera.client.addons;

public abstract class AbstractAddon {
    public String modId;
    public String name;

    public abstract void onInitialize();
    public void onExit() {}
}
