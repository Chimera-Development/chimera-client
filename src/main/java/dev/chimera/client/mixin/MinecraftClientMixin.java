package dev.chimera.client.mixin;

import dev.chimera.client.ChimeraClient;
import dev.chimera.client.amalthea.modules.Konami;
import dev.chimera.client.amalthea.modules.Maxwell;
import dev.chimera.client.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "getMusicType",at = @At("HEAD"), cancellable = true)
    public void getMusicType(CallbackInfoReturnable<MusicSound> cir){
        if (ModuleManager.getModule(Maxwell.class).isActive()) {
            cir.setReturnValue(ChimeraClient.maxwellSound);
        }
//        } else if (MinecraftClient.getInstance().currentScreen instanceof Konami.SecretScreen && !ModuleManager.getModule(Maxwell.class).isActive()){
//            //System.out.println("playing the secret music");
//            cir.setReturnValue(ChimeraClient.secretSound);
//        }
    }
}
