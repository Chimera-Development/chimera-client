package dev.chimera.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("chimera-client");
    public static final String MOD_ID = "chimera-client";

    @Override
    public void onInitializeClient() {

    }
}