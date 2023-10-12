package dev.chimera.client;

import dev.chimera.client.addons.AddonManager;
import dev.chimera.client.gui.TabGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    public static final String MOD_ID = "chimera-client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        AddonManager.loadAddons();

        Screen tabGUI = new TabGUI(Text.literal("Tab GUI"));
        HudRenderCallback.EVENT.register((drawContext, delta) -> {
            tabGUI.render(drawContext, 0, 0, delta);
        });
    }
}