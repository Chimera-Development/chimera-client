package dev.chimera.client.gui;

import dev.chimera.client.util.ColorHolder;
import dev.chimera.client.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.joml.Vector2d;

public class TestScreen extends Screen {
    protected TestScreen() {
        super(Text.of("Test"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //super.render(context, mouseX, mouseY, delta);
        Window window = MinecraftClient.getInstance().getWindow();
        RenderUtil.drawFilledRoundedRectangle(40,40,200,200,50,32,new ColorHolder());
        //RenderUtil.drawArcFilled(new Vector2d(window.getScaledWidth()/2,window.getScaledHeight()/2),window.getScaledHeight()/2,new Pair<>(90f,180f), 3,new ColorHolder());
    }
}
