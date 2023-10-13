package dev.chimera.client.gui.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

public class TextWidget implements Drawable {
    private int x;
    private int y;
    private Text text;
    private final TextRenderer textRenderer;

    public TextWidget(int x, int y, Text text, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.textRenderer = textRenderer;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawText(this.textRenderer, this.text, this.getX(), this.getY(), 0xffffff, false);
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Text getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
