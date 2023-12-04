package dev.chimera.client.modules;

import dev.chimera.client.events.KeyEvent;
import net.engio.mbassy.bus.MessagePublication;
import net.engio.mbassy.dispatch.HandlerInvocation;
import net.engio.mbassy.subscription.SubscriptionContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static dev.chimera.client.ChimeraClient.KEYBIND_MANAGER;
import static net.minecraft.util.math.MathHelper.lerp;

public abstract class Module {
    private final String name;
    private final String description;
    public int keyBinding;
    public boolean stickyKey = false;
    private boolean active;
    private ModuleCategory category;

    public Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.keyBinding = -1;
        this.active = false;
        this.category = category;
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

    public Module(String name, String description, ModuleCategory category, int keyBinding) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBinding = keyBinding;
        this.active = false;

        KEYBIND_MANAGER.registerModuleKeybind(this);
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
            MinecraftClient.getInstance().player.sendMessage(Text.of("Set " + name + " to disabled"));
        } else {
            onEnable();
            MinecraftClient.getInstance().player.sendMessage(Text.of("Set " + name + " to enabled"));
        }
    }

    public void setActive(boolean isActive) {
        if (isActive == active) return;
        if (isActive) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }
}