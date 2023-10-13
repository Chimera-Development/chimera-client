package dev.chimera.client.system;

import dev.chimera.client.events.KeyEvent;
import net.engio.mbassy.bus.MessagePublication;
import net.engio.mbassy.dispatch.HandlerInvocation;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.subscription.SubscriptionContext;
import net.minecraft.client.MinecraftClient;

import static dev.chimera.client.ChimeraClient.EVENT_MANAGER;

public abstract class Module {
    private final String name;
    private final String description;
    private int keyBinding;
    private boolean active;

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.keyBinding = -1;
        this.active = false;
    }

    public static class KeybindListener extends HandlerInvocation<Module, KeyEvent> {

        public KeybindListener(SubscriptionContext context) {
            super(context);
        }

        @Override
        public void invoke(Module module, KeyEvent event, MessagePublication publication) {
            if (event.key == module.keyBinding) {
                module.onKeyBind(event);
            }
        }
    }

    public Module(String name, String description, int keyBinding) {
        this.name = name;
        this.description = description;
        this.keyBinding = keyBinding;
        this.active = false;
    }

    public void onEnable() {
        active = true;
    }

    public void onDisable() {
        active = false;
    }

    public void onKeyBind(KeyEvent event) {
        toggle();
    }


    public void onInit() {
    }


    public void toggle() {
        if (active) {
            onDisable();
        } else {
            onEnable();
        }
    }

    public boolean isActive() {
        return active;
    }
}
