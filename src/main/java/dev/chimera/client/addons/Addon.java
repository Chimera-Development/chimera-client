package dev.chimera.client.addons;

import net.minecraft.client.MinecraftClient;

public abstract class Addon {
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public String modId;
    public String name;

    public abstract void onInitialize();
    public void onExit() {}
}