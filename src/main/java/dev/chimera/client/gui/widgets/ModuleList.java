package dev.chimera.client.gui.widgets;


import dev.chimera.client.gui.ClickGUI;
import dev.chimera.client.modules.Module;
import dev.chimera.client.modules.ModuleCategory;
import dev.chimera.client.modules.ModuleManager;
import dev.chimera.client.util.ColorHolder;
import dev.chimera.client.util.RenderUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

import java.awt.*;
import java.util.ArrayList;


import static dev.chimera.client.addons.Addon.mc;


public class ModuleList implements Drawable {
    private ModuleCategory category;
    private int x = 70;
    private int y = 10;
    private int ySpacing = 20;
    private int xSpacing = 5;
    private int maxWidth = 20;
    private int height = 200;
    private boolean mouseDown;
    private boolean mouseOver;
    private boolean dragable;
    private boolean firstClick;
    private int dragStartX;
    private int dragStartY;
    private int prevX;
    private int prevY;
    private ModuleButton[] moduleButtons;
    TextRenderer textRenderer;
    public ModuleList(ModuleCategory category,int x, int y){
        this.category = category;
        this.x = x;
        this.y = y;
        this.firstClick = true;
        Module[] modules;
        try {
            modules = ModuleManager.getModulesByCategory(category);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        this.textRenderer = mc.textRenderer;
        for (int i = 0; i < modules.length; i++) {
            if (textRenderer.getWidth(modules[i].getName()) > maxWidth) maxWidth = textRenderer.getWidth(modules[i].getName());
        }
        this.moduleButtons = new ModuleButton[modules.length];
        System.out.println(modules.length);
        for (int i = 0; i < moduleButtons.length; i++) {
            moduleButtons[i] = new ModuleButton(modules[i],x,y+20+ySpacing-5+i*ySpacing,maxWidth,xSpacing,ySpacing);
        }
    }
//    public ModuleList(Text title,int x, int y){
//        this.title = title;
//        this.x = x;
//        this.y = y;
//    }
//    public ModuleList(String title,int x, int y){
//        this.title = Text.of(title);
//        this.x = x;
//        this.y = y;
//    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.textRenderer = mc.textRenderer;
        ClickGUI clickGUI = (ClickGUI)ModuleManager.getModule(ClickGUI.class);
        ArrayList<Boolean> notOvers = new ArrayList<>();
        for (ModuleList moduleList : clickGUI.getScreen().getModuleLists()) {
            int[] boundingBox = moduleList.getBoundingBox();
            if (!(mouseX > boundingBox[0] && mouseX < boundingBox[2] && mouseY > boundingBox[1] && mouseY < boundingBox[3])){
                notOvers.add(true);
            }
        }
        boolean notOver = !notOvers.contains(false);
        if(mouseX > x && mouseX < x+maxWidth+xSpacing*2 && mouseY > y && mouseY < y+20){
            if (firstClick && mouseDown  && notOver){
                this.dragStartX = mouseX;
                this.dragStartY = mouseY;
                this.prevX = x;
                this.prevY = y;
                firstClick = false;
                System.out.println(String.format("First Click at %d %d",dragStartX,dragStartY));
            }

            this.dragable = true;
        } else {
            this.firstClick = true;
            this.dragable = false;
        }
        if(mouseDown && dragable  && notOver){
            this.x = mouseX-(dragStartX-prevX);
            this.y = mouseY-(dragStartY-prevY);
        }
        RenderUtil.drawFilledRoundedRectangle(x,y,maxWidth+xSpacing*2,200,5,64,ColorHolder.from(Color.RED));
        context.drawText(textRenderer,category.title,x+xSpacing+(maxWidth/2)-(textRenderer.getWidth(category.title)/2),y+5,-1,false);
        context.drawHorizontalLine(x,(x+maxWidth+xSpacing*2)-1,y+20,-1);
        if (moduleButtons != null){
            for (int i = 0; i < moduleButtons.length; i++) {
                moduleButtons[i].setMouseDown(mouseDown);
                moduleButtons[i].updateXY(x,y+20+ySpacing-5+i*ySpacing);
                moduleButtons[i].render(context,mouseX,mouseY,delta);
            }
        }
    }

    public void setMouseDown(boolean mouseDown) {
        this.mouseDown = mouseDown;
    }

    public int[] getBoundingBox(){
        return new int[]{x,y,x+maxWidth+xSpacing*2,height};
    }

}
