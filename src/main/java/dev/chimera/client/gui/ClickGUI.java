package dev.chimera.client.gui;

import dev.chimera.client.events.KeyEvent;
import dev.chimera.client.events.RenderEvent;
import dev.chimera.client.modules.Module;
import dev.chimera.client.modules.ModuleCategory;
import net.engio.mbassy.listener.Handler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGUI extends Module {
    private final ClickGUIScreen clickGUIScreen;
    private Screen previousScreen;
    public ClickGUI() {
        super("ClickGUI","The GUI you click", ModuleCategory.GUI, GLFW.GLFW_KEY_RIGHT_SHIFT);
        this.clickGUIScreen = new ClickGUIScreen(Text.literal("Click GUI"));
        //this.testScreen = new TestScreen();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        previousScreen = MinecraftClient.getInstance().currentScreen;
        //System.out.println("set previous to " + previousScreen.getTitle());
        MinecraftClient.getInstance().setScreen(clickGUIScreen);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftClient.getInstance().setScreen(previousScreen);
    }

    @Handler
    public void onKeyBind(KeyEvent event) {
        System.out.println(event.key);
        if(event.key == this.keyBinding || event.key == GLFW.GLFW_KEY_ESCAPE){
            this.setActive(false);
        }
    }

    public ClickGUIScreen getScreen() {
        return clickGUIScreen;
    }
}
