package dev.chimera.client;

import dev.chimera.client.addons.AddonManager;
import dev.chimera.client.gui.TabGUIScreen;
import dev.chimera.client.modules.KeybindManager;
import dev.chimera.client.util.ModuleCategories;
import dev.chimera.client.util.TabTree;
import net.engio.mbassy.bus.MBassador;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ChimeraClient implements ClientModInitializer {


    public static final String MOD_ID = "chimera-client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @SuppressWarnings("rawtypes")
    public static final MBassador EVENT_MANAGER = new MBassador(error -> {
        LOGGER.error(error.getMessage());
    });
    public static final KeybindManager KEYBIND_MANAGER = new KeybindManager();

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            AddonManager.loadAddons();

            TabTree tabTree = new TabTree(Text.literal("Modules"));
            Field[] moduleCategories = ModuleCategories.class.getDeclaredFields();
            for (Field field : moduleCategories) {
                try {
                    field.setAccessible(true);
                    TabTree tree = (TabTree) field.get(moduleCategories);
                    tabTree.put(tree.getTitle().getString(), tree);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            TabGUIScreen tabGUIScreen = new TabGUIScreen(Text.literal("Tab GUI"), tabTree);
            HudRenderCallback.EVENT.register((drawContext, delta) -> {
                tabGUIScreen.render(drawContext, 0, 0, delta);
            });
        });
    }
}