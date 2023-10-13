package dev.chimera.client.gui.widgets;

import dev.chimera.client.util.TabTree;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

import java.util.List;

public class TabNodeWidget implements Drawable {
    TabTree node;
    public TabNodeWidget(TabTree node) {
        this.node = node;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int xPos = node.getLayer() * 64;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(textRenderer, node.getTitle(), xPos, 0, 0xffffff, false);
        List<String> keys = node.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            context.drawText(textRenderer, key, xPos, (i + 1) * 8, 0xdddddd, false);
        }
    }
}