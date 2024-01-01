package dev.chimera.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.events.KeyEvent;
import dev.chimera.client.gui.widgets.ModuleList;
import dev.chimera.client.modules.ModuleCategory;
import dev.chimera.client.modules.ModuleManager;
import dev.chimera.client.util.ModuleCategories;
import net.engio.mbassy.listener.Handler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;


public class ClickGUIScreen extends Screen {
    Profiler profiler = MinecraftClient.getInstance().getProfiler();
    private boolean firstRelease = true;
    private int x = 70;
    private int y = 10;
    private int xSpacing = 50;
    private boolean mouseDown;
    ModuleList[] moduleLists;
    public ClickGUIScreen(Text title) {
        super(title);
        this.moduleLists = new ModuleList[ModuleCategory.values().length];
        System.out.println("Created ClickGUIScreen");
        for (int i = 0; i < moduleLists.length; i++) {
            System.out.println("Created ModuleList " + i);
            moduleLists[i] = new ModuleList(ModuleCategory.values()[i],x+xSpacing*i,y);
        }
        for (ModuleList moduleList : moduleLists) {
            System.out.println(Arrays.toString(moduleList.getBoundingBox()));
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        for (ModuleList moduleList : moduleLists) {
            moduleList.setMouseDown(mouseDown);
            moduleList.render(context,mouseX,mouseY,delta);
        }
    }



    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT && !firstRelease) {
             ModuleManager.getModule(ClickGUI.class).setActive(false);
        }
        firstRelease = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ESCAPE){
            ModuleManager.getModule(ClickGUI.class).setActive(false);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onDisplayed() {
        firstRelease = true;
        super.onDisplayed();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseDown = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.mouseDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public ModuleList[] getModuleLists() {
        return moduleLists;
    }
}