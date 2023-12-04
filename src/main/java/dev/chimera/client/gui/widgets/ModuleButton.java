package dev.chimera.client.gui.widgets;


import dev.chimera.client.modules.Module;
import dev.chimera.client.util.ColorHolder;
import dev.chimera.client.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

import java.awt.*;


public class ModuleButton implements Drawable {

    private Module module;
    private int x;
    private int y;
    private int width;
    private int height = 15;
    private int xSpacing;
    private int ySpacing;
    private TextRenderer textRenderer;
    private boolean mouseOver;
    private boolean mouseDown;
    private boolean firstClick = true;

    private ColorHolder standardColor = new ColorHolder(0,0,0,255);
    //private ColorHolder hoverColor = new ColorHolder(25,25,25,255);
    //private ColorHolder hoverColor = ColorHolder.random();

    public ModuleButton(Module module,int x, int y, int maxWidth, int xSpacing, int ySpacing){
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = maxWidth;
        this.xSpacing = xSpacing;
        this.ySpacing = ySpacing;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        System.out.println("Created ModuleButton for " + module.getName());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (mouseX > x &&
            mouseX < x+width+xSpacing*2 &&
            mouseY > y-height/3 &&
            mouseY < y-height/3+height)
        {
            mouseOver = true;
        } else {
            mouseOver = false;
        }
        if (mouseDown && mouseOver && firstClick) {
            module.toggle();
            firstClick = false;
        }
        if (!mouseDown && mouseOver && !firstClick){
            firstClick = true;
        }

        ColorHolder color = mouseOver ? ColorHolder.from(Color.RED) : standardColor;
        //RenderUtil.drawFilledRectangle(x,y-height/3,width+xSpacing*2,height, color);
        RenderUtil.drawFilledRoundedRectangle(x,y-height/3,width+xSpacing*2,height,3, 64,color);
        context.drawText(textRenderer,module.getName(),x+xSpacing,y,-1,false);
        //System.out.println(firstClick);
    }
    public void updateXY(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setMouseDown(boolean mouseDown) {
        this.mouseDown = mouseDown;
    }
}
