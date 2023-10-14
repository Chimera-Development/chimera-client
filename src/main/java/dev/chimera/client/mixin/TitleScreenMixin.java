package dev.chimera.client.mixin;

import dev.chimera.client.events.RenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.chimera.client.ChimeraClient.EVENT_MANAGER;
import static dev.chimera.client.ChimeraClient.LOGGER;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique
    RenderEvent renderEvent = new RenderEvent();

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        renderEvent.drawContext = context;
        EVENT_MANAGER.post(renderEvent).now();
    }
}
