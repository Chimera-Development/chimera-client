package dev.chimera.client.addons;

import net.minecraft.client.MinecraftClient;

public abstract class AbstractAddon {
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public String modId;
    public String name;

    public abstract void onInitialize();
    public void onExit() {}
}