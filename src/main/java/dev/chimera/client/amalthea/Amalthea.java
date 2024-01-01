package dev.chimera.client.amalthea;

import dev.chimera.client.ChimeraClient;
import dev.chimera.client.addons.Addon;
import dev.chimera.client.amalthea.modules.KillAura;
import dev.chimera.client.amalthea.modules.Konami;
import dev.chimera.client.amalthea.modules.Maxwell;
import dev.chimera.client.modules.ModuleManager;
import dev.chimera.client.amalthea.modules.Trollface;
import dev.chimera.client.util.ModuleCategories;
import net.minecraft.util.Identifier;

public class Amalthea extends Addon {
    @Override
    public void onInitialize() {
        ModuleManager.loadModule(Trollface.class, new Identifier(ChimeraClient.MOD_ID, "icon.png"));
        ModuleManager.loadModule(Maxwell.class);
        ModuleManager.loadModule(Konami.class);
        ModuleManager.loadModule(KillAura.class);
    }
}
