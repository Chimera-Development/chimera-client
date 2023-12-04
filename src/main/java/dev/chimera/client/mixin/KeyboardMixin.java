package dev.chimera.client.mixin;

import dev.chimera.client.amalthea.modules.Konami;
import dev.chimera.client.amalthea.modules.Maxwell;
import dev.chimera.client.events.KeyEvent;
import dev.chimera.client.modules.ModuleManager;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static dev.chimera.client.ChimeraClient.EVENT_MANAGER;
import static dev.chimera.client.ChimeraClient.LOGGER;
import static org.lwjgl.glfw.GLFW.*;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Unique
    KeyEvent keyEvent = new KeyEvent();
    private int combo = 0;
    @Unique
    private boolean triggered = false;
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        Konami konami = (Konami) ModuleManager.getModule(Konami.class);
        PositionedSoundInstance correctSound = PositionedSoundInstance.ambient(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
        PositionedSoundInstance triggerSound = PositionedSoundInstance.ambient(SoundEvents.ENTITY_PLAYER_LEVELUP,1f,1f);
        PositionedSoundInstance wrongSound = PositionedSoundInstance.ambient(SoundEvents.ENTITY_VILLAGER_NO,1f,1f);
        //System.out.println("Pressed " + key);
        keyEvent.key = key;
        keyEvent.pressed = action >= 1;
        if (keyEvent.pressed && MinecraftClient.getInstance().currentScreen instanceof TitleScreen){
            if (combo == 0 && key == GLFW_KEY_UP){
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 1 && key == GLFW_KEY_UP) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 2 && key == GLFW_KEY_DOWN) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 3 && key == GLFW_KEY_DOWN) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 4 && key == GLFW_KEY_LEFT) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 5 && key == GLFW_KEY_RIGHT) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 6 && key == GLFW_KEY_LEFT) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 7 && key == GLFW_KEY_RIGHT) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 8 && key == GLFW_KEY_B) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 9 && key == GLFW_KEY_A) {
                combo++;
                System.out.println(String.format("combo++, combo: %d",combo));
                MinecraftClient.getInstance().getSoundManager().play(correctSound);
            } else if (combo == 10 && key == GLFW_KEY_ENTER){
                info.cancel();
                combo = 0;
                konami.setTriggered(true);
                triggered = true;
                System.out.println(String.format("code triggered, combo: %d",combo));
                ModuleManager.getModule(Maxwell.class).setActive(false);
                MinecraftClient.getInstance().getSoundManager().play(triggerSound);
            } else {
                if (combo != 0){
                    combo = 0;
                    konami.setTriggered(false);
                    triggered = false;
                    System.out.println("fucked up code");
                    MinecraftClient.getInstance().getSoundManager().play(wrongSound);
                };
            }
        }
        EVENT_MANAGER.post(keyEvent).now();
    }
}
