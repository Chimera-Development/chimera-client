package dev.chimera.client;

import com.mojang.datafixers.kinds.Kind1;
import dev.chimera.client.addons.AddonManager;
import dev.chimera.client.gui.ClickGUI;
import dev.chimera.client.gui.ClickGUIScreen;
import dev.chimera.client.gui.TabGUIScreen;
import dev.chimera.client.modules.KeybindManager;
import dev.chimera.client.modules.ModuleManager;
import dev.chimera.client.util.ModuleCategories;
import dev.chimera.client.util.TabTree;
import net.engio.mbassy.bus.MBassador;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    private static final Identifier MAXWELL_SOUND = new Identifier(MOD_ID,"maxwell");
    public static SoundEvent maxwellSoundEvent = SoundEvent.of(MAXWELL_SOUND);
    private static final Identifier SECRET_SOUND = new Identifier(MOD_ID,"secret");
    public static SoundEvent secretSoundEvent = SoundEvent.of(SECRET_SOUND);
    //public static  MusicSound secretSound = new MusicSound(Registries.SOUND_EVENT.getEntry(secretSoundEvent),12000,24000,true);
    public static MusicSound secretSound = new MusicSound(Registries.SOUND_EVENT.getEntry(secretSoundEvent),0,1,false);
    public static MusicSound maxwellSound = new MusicSound(Registries.SOUND_EVENT.getEntry(ChimeraClient.maxwellSoundEvent), 12000, 24000, false);

    @Override
    public void onInitializeClient() {
        Registry.register(Registries.SOUND_EVENT,MAXWELL_SOUND,maxwellSoundEvent);
        Registry.register(Registries.SOUND_EVENT,SECRET_SOUND,secretSoundEvent);
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            AddonManager.loadAddons();
            ModuleManager.loadModule(ClickGUI.class);

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