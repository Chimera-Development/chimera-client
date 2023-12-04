package dev.chimera.client.amalthea.modules;

import dev.chimera.client.modules.Module;
import dev.chimera.client.modules.ModuleCategory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import static dev.chimera.client.addons.Addon.mc;

public class KillAura extends Module {
    public KillAura(){
        super("KillAura","kills fast as fuck boi", ModuleCategory.COMBAT, GLFW.GLFW_KEY_R);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        WorldTickCallback.EVENT.register((listener)->{
            if(!this.isActive()) return;
            if (mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f || true){
                for (Entity entity : mc.world.getEntities()) {
                    if (!entity.equals(mc.player) && entity.isAlive()) {
                        if (entity.distanceTo(mc.player) < 3.0) {
                            boolean wasSprinting = mc.player.isSprinting();
                            if (wasSprinting) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                            if (entity.isAttackable()) {
                                mc.interactionManager.attackEntity(mc.player, entity);
                                mc.player.swingHand(Hand.MAIN_HAND);
                            }
                            if (wasSprinting)
                                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                            System.out.println("Attacking " + entity.getEntityName());
                        }
                    }
                }
            }
        });
    }
}
