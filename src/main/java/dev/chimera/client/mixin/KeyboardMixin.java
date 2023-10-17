package dev.chimera.client.mixin;

import dev.chimera.client.events.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.chimera.client.ChimeraClient.EVENT_MANAGER;
import static dev.chimera.client.ChimeraClient.LOGGER;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Unique
    KeyEvent keyEvent = new KeyEvent();

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {

        keyEvent.key = key;
        keyEvent.pressed = action >= 1;
        EVENT_MANAGER.post(keyEvent).now();
    }
}
