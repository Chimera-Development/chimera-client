package dev.chimera.client;

import dev.chimera.client.addons.AddonManager;
import dev.chimera.client.gui.TabGUIScreen;
import dev.chimera.client.modules.AbstractModule;
import dev.chimera.client.util.TabTree;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    public static final String MOD_ID = "chimera-client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        AddonManager.loadAddons();

        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            TabTree tabTree = new TabTree(Text.literal("test 1"));
            tabTree.put("test 2", new TabTree(Text.literal("test 3")));
            tabTree.put("test 4", new AbstractModule() {});
            tabTree.pathForwards("test 2");
            TabGUIScreen tabGUIScreen = new TabGUIScreen(Text.literal("Tab GUI"), tabTree);
            HudRenderCallback.EVENT.register((drawContext, delta) -> {
                tabGUIScreen.render(drawContext, 0, 0, delta);
            });
        });
    }
}