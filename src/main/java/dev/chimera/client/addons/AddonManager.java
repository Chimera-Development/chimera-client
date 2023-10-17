package dev.chimera.client.addons;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;
import java.util.List;

public class AddonManager {
    public static List<Addon> ADDON_LIST = new ArrayList<>();

    public static void loadAddons() {
        List<EntrypointContainer<Addon>> entrypoint
                = FabricLoader.getInstance().getEntrypointContainers("chimera-addon", Addon.class);
        for (EntrypointContainer<Addon> entrypointContainer : entrypoint) {
            ModMetadata metadata = entrypointContainer.getProvider().getMetadata();
            Addon addon = entrypointContainer.getEntrypoint();

            if (addon.modId == null) addon.modId = metadata.getId();
            if (addon.name == null) addon.name = metadata.getName();

            ADDON_LIST.add(addon);
        }

        // call onInitialize after all addons are loaded
        ADDON_LIST.forEach(Addon::onInitialize);
    }

    public static void onExit() {
        // call the onExit function of each addon when the game is closed
        ADDON_LIST.forEach(Addon::onExit);
    }
}