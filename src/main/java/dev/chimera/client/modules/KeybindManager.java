package dev.chimera.client.modules;

import dev.chimera.client.events.KeyEvent;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static dev.chimera.client.ChimeraClient.EVENT_MANAGER;
import static dev.chimera.client.ChimeraClient.LOGGER;

@Listener(references = References.Strong)
public class KeybindManager {


    public KeybindManager() {
        EVENT_MANAGER.subscribe(this);
    }

    private Map<Integer, List<Consumer<Boolean>>> keyListeners = new HashMap<>();

    @Handler(rejectSubtypes = false)
    public void onKeyPress(KeyEvent event) {

        List<Consumer<Boolean>> listeners = keyListeners.get(event.key);
        listeners.forEach(listener -> {
            listener.accept(event.pressed);
        });
    }

    public void registerModuleKeybind(Module module) {
        if (module.stickyKey) {
            keyListeners.computeIfAbsent(module.keyBinding, pressed -> new ArrayList<>()).add(__ -> module.toggle());
        } else {
            keyListeners.computeIfAbsent(module.keyBinding, pressed -> new ArrayList<>()).add(pressed -> {
                if (pressed) {
                    module.toggle();
                }
            });
        }
    }

    public void registerKeybind(int keyBind, Consumer<Boolean> listener) {
        keyListeners.computeIfAbsent(keyBind, k -> new ArrayList<>()).add(listener);
    }

    public void unregisterModuleKeybind(Module module) {
        List<Consumer<Boolean>> listeners = keyListeners.get(module.keyBinding);
        if (module.stickyKey) {
            listeners.remove((Consumer<Boolean>) __ -> module.toggle());
        } else {
            listeners.remove((Consumer<Boolean>) pressed -> {
                if (pressed) {
                    module.toggle();
                }
            });
        }
    }

    public void unregisterKeybind(int keyBind, Consumer<Boolean> listener) {
        List<Consumer<Boolean>> listeners = keyListeners.get(keyBind);
        listeners.remove(listener);
    }

}
