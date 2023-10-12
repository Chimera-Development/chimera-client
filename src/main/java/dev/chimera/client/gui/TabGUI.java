package dev.chimera.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class TabGUI extends Screen {
    public TabGUI(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        // idk do something
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawables.forEach(drawable -> {
            drawable.render(context, mouseX, mouseY, delta);
        });
    }
}
