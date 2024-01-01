package dev.chimera.client.mixin;

import dev.chimera.client.ChimeraClient;
import dev.chimera.client.amalthea.modules.Konami;
import dev.chimera.client.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin {
    Konami.SecretScreen secretScreen = new Konami.SecretScreen();

    @Inject(method = "render",at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        Konami konami = (Konami) ModuleManager.getModule(Konami.class);
        //System.out.println(konami.isTriggered());
        if (konami.isTriggered()){
            System.out.println("Rendering while triggered");
            ci.cancel();
            MinecraftClient.getInstance().setScreen(secretScreen);

        }
    }
//    @Inject(method = "getMusic",at = @At("HEAD"), cancellable = true)
//    public void getMusic(CallbackInfoReturnable<MusicSound> cir) {
//        MinecraftClient.getInstance().getMusicTracker().stop();
//        System.out.println("returning secret sound");
//        cir.setReturnValue(ChimeraClient.secretSound);
//    }
//    @Inject(method = "removed",at = @At("HEAD"), cancellable = true)
//    public void removed(CallbackInfo ci) {
//        MinecraftClient.getInstance().getMusicTracker().stop(ChimeraClient.secretSound);
//        ci.cancel();
//    }
}
