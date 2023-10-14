package dev.chimera.client.amalthea;

import dev.chimera.client.ChimeraClient;
import dev.chimera.client.addons.AbstractAddon;
import dev.chimera.client.modules.ModuleManager;
import dev.chimera.client.amalthea.modules.Trollface;
import dev.chimera.client.util.ModuleCategories;
import net.minecraft.util.Identifier;

public class Amalthea extends AbstractAddon {
    @Override
    public void onInitialize() {
        ModuleManager.loadModule(ModuleCategories.MISC, Trollface.class, new Identifier(ChimeraClient.MOD_ID, "trollface.png"));
    }
}
