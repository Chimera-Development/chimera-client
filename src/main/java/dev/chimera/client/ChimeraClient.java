package dev.chimera.client;

import dev.chimera.client.modules.Trollface;
import dev.chimera.client.system.ModuleLoader;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("chimera-client");
    public static final String MOD_ID = "chimera-client";
    @SuppressWarnings("rawtypes")
    public static final MBassador EVENT_MANAGER = new MBassador(new IPublicationErrorHandler() {
        @Override
        public void handleError(PublicationError error) {
            LOGGER.error(error.getMessage());
        }
    });

    @Override
    public void onInitialize() {


        ModuleLoader.loadModule(Trollface.class, new Identifier(MOD_ID, "icon.png"));

        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");
    }
}