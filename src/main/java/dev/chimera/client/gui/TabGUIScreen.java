package dev.chimera.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.util.TabTree;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.text.Text;

import java.util.List;

public class TabGUIScreen extends Screen {
    private final TabTree tabTree;

    public TabGUIScreen(Text title, TabTree tabTree) {
        super(title);
        this.tabTree = tabTree;
        this.init(MinecraftClient.getInstance(), 0, 0);
    }

    @Override
    protected void init() {
        // this.addDrawable(new TextWidget(16, 16, Text.literal("look patore i did a thing"), this.textRenderer));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        this.drawables.forEach(drawable -> {
            drawable.render(context, mouseX, mouseY, delta);
        });

        {
            List<Object> path = this.tabTree.getPathObjects();
            for (Object obj : path) {
                ((TabTree)obj).render(context, mouseX, mouseY, delta);
            }
        }

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
}
