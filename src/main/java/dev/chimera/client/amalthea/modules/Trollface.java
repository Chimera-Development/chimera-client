package dev.chimera.client.amalthea.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.events.RenderEvent;
import dev.chimera.client.modules.Module;
import dev.chimera.client.modules.ModuleCategory;
import dev.chimera.client.util.ColorHolder;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import static dev.chimera.client.ChimeraClient.*;
//import static dev.chimera.client.ChimeraClient.vertexHelper;

@Listener(references = References.Strong)
public class Trollface extends Module {

    private int coordX = 0;
    private int coordY = 0;
    private static int width = 128;
    private static int height = 128;
    private static int depth = 128;
    private boolean movingDown = false;
    private boolean movingRight = false;
    float rotationAmount = 1.0f;
    float stepsize = rotationAmount;
    private static boolean colorImages = false;


    private static ColorHolder color1 = new ColorHolder(255,0,0,255);
    private static ColorHolder color2 = new ColorHolder(0,255,0,255);
    private static ColorHolder color3 = new ColorHolder(0,0,255,255);
    private static ColorHolder color4 = new ColorHolder(0,255,255,255);
    private static ColorHolder color5 = new ColorHolder(255,255,0,255);
    private static ColorHolder color6 = new ColorHolder(255,255,255,255);

    private MinecraftClient MC = MinecraftClient.getInstance();
    Identifier imagePath = new Identifier(MOD_ID, "trollface.png");

    public Trollface() {
        super("Trollface", "Displays a bouncing trollface on your screen", ModuleCategory.GUI, GLFW.GLFW_KEY_G);
        //this.onEnable();
    }

    public Trollface(Identifier imagePath) {
        this();
        this.imagePath = imagePath;
    }

    @Handler
    public void render(RenderEvent event) {
        //RenderSystem.disableDepthTest();
        DrawContext drawContext = event.drawContext;
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        checkedMove();
        //event.drawContext.drawTexture(imagePath, coordX, coordY, 0, 0, width, height);
        MinecraftClient mc = MinecraftClient.getInstance();

        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull(); //TODO: Temporary fix. reverse wrong faces todo
        checkedMove();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // White
        //float angle = (float)Math.toRadians((coordX % (mc.getWindow().getScaledHeight()+360.0f)*(movingDown?-1:1)));
        float angle1 = (float)Math.toRadians(coordY % (drawContext.getScaledWindowHeight()+360.0f))*rotationAmount;
        float angle2 = (float)Math.toRadians(coordX % (drawContext.getScaledWindowWidth()+360.0f))*rotationAmount;
        //angle = (float)Math.toRadians(230.0f);

        Quaternionf quaternionf = new Quaternionf().rotateXYZ(angle1,angle2,0).normalize();
        Matrix4f move = new Matrix4f().translate(coordX+(width/2),coordY+(height/2),depth/2);

        // Front Face
        Vector4f fpos1 = new Vector4f(-(width/2),-(height/2),-(depth/2),1); //Top left
        Vector4f fpos2 = new Vector4f(-(width/2),(height/2),-(depth/2),1);  //Bottom left
        Vector4f fpos3 = new Vector4f(width/2,-(height/2),-(depth/2),1); //Top Right
        Vector4f fpos4 = new Vector4f(width/2,(height/2),-(depth/2),1);  //Bottom Right

        //Left Face
        Vector4f lpos1 = new Vector4f(-(width/2),-(height/2),-(depth/2),1);
        Vector4f lpos2 = new Vector4f(-(width/2),(height/2),-(depth/2),1);
        Vector4f lpos3 = new Vector4f(-(width/2),-(height/2),depth/2,1);
        Vector4f lpos4 = new Vector4f(-(width/2),(height/2),depth/2,1);

        //Back Face
        Vector4f bpos1 = new Vector4f((width/2),-(height/2),depth/2,1);
        Vector4f bpos2 = new Vector4f((width/2),(height/2),depth/2,1);
        Vector4f bpos3 = new Vector4f(-(width/2),-(height/2),depth/2,1);
        Vector4f bpos4 = new Vector4f(-(width/2),(height/2),depth/2,1);

        //Right Face
        Vector4f rpos1 = new Vector4f((width/2),-(height/2),depth/2,1);
        Vector4f rpos2 = new Vector4f((width/2),(height/2),depth/2,1);
        Vector4f rpos3 = new Vector4f((width/2),-(height/2),-(depth/2),1);
        Vector4f rpos4 = new Vector4f((width/2),(height/2),-(depth/2),1);

        //Top Face
        Vector4f tpos1 = new Vector4f(-(width/2),-(height/2),-(depth/2),1);
        Vector4f tpos2 = new Vector4f((width/2),-(height/2),-(depth/2),1);
        Vector4f tpos3 = new Vector4f(-(width/2),-(height/2),depth/2,1);
        Vector4f tpos4 = new Vector4f((width/2),-(height/2),depth/2,1);

        //Bottom Face
        Vector4f btpos1 = new Vector4f((width/2),(height/2),depth/2,1);
        Vector4f btpos2 = new Vector4f(-(width/2),(height/2),depth/2,1);
        Vector4f btpos3 = new Vector4f((width/2),(height/2),-(depth/2),1);
        Vector4f btpos4 = new Vector4f(-(width/2),(height/2),-(depth/2),1);

        Vector4f[] vertices = new Vector4f[]
        {
                fpos1,fpos2,fpos3,fpos4, //Green to Blue
                lpos1,lpos2,lpos3,lpos4, //Full Green
                bpos1,bpos2,bpos3,bpos4, //Red to Green
                rpos1,rpos2,rpos3,rpos4,//Light Blue
                tpos1,tpos2,tpos3,tpos4,// Yellow
                btpos1,btpos2,btpos3,btpos4 //White
        };

//        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
//        bufferBuilder.vertex(matrix,fpos1.x+coordX+(width/2),fpos1.y+coordY+(height/2),fpos1.z).color(color1.getARGB()).next();
//        bufferBuilder.vertex(matrix,fpos2.x+coordX+(width/2),fpos2.y+coordY+(height/2),fpos2.z).color(color1.getARGB()).next();
//        bufferBuilder.vertex(matrix,fpos3.x+coordX+(width/2),fpos3.y+coordY+(height/2),fpos3.z).color(color1.getARGB()).next();
//        bufferBuilder.vertex(matrix,fpos4.x+coordX+(width/2),fpos4.y+coordY+(height/2),fpos4.z).color(color1.getARGB()).next();
//        tessellator.draw();
        for (Vector4f vertex : vertices){
            vertex = quaternionf.transform(vertex);
            vertex.mul(move);
            //vertex.mul(rotateX);
            //vertex.mul(rotateY);

        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(matrix,fpos1.x,fpos1.y,fpos1.z).color(color1.getARGB()).next();
        bufferBuilder.vertex(matrix,fpos2.x,fpos2.y,fpos2.z).color(color1.getARGB()).next();
        bufferBuilder.vertex(matrix,fpos4.x,fpos4.y,fpos4.z).color(color1.getARGB()).next();
        bufferBuilder.vertex(matrix,fpos3.x,fpos3.y,fpos3.z).color(color1.getARGB()).next();


        bufferBuilder.vertex(matrix,lpos1.x,lpos1.y,lpos1.z).color(color2.getARGB()).next();
        bufferBuilder.vertex(matrix,lpos2.x,lpos2.y,lpos2.z).color(color2.getARGB()).next();
        bufferBuilder.vertex(matrix,lpos4.x,lpos4.y,lpos4.z).color(color2.getARGB()).next();
        bufferBuilder.vertex(matrix,lpos3.x,lpos3.y,lpos3.z).color(color2.getARGB()).next();


        bufferBuilder.vertex(matrix,bpos1.x,bpos1.y,bpos1.z).color(color3.getARGB()).next();
        bufferBuilder.vertex(matrix,bpos2.x,bpos2.y,bpos2.z).color(color3.getARGB()).next();
        bufferBuilder.vertex(matrix,bpos4.x,bpos4.y,bpos4.z).color(color3.getARGB()).next();
        bufferBuilder.vertex(matrix,bpos3.x,bpos3.y,bpos3.z).color(color3.getARGB()).next();


        bufferBuilder.vertex(matrix,rpos1.x,rpos1.y,rpos1.z).color(color4.getARGB()).next();
        bufferBuilder.vertex(matrix,rpos2.x,rpos2.y,rpos2.z).color(color4.getARGB()).next();
        bufferBuilder.vertex(matrix,rpos4.x,rpos4.y,rpos4.z).color(color4.getARGB()).next();
        bufferBuilder.vertex(matrix,rpos3.x,rpos3.y,rpos3.z).color(color4.getARGB()).next();

        bufferBuilder.vertex(matrix,tpos1.x,tpos1.y,tpos1.z).color(color5.getARGB()).next();
        bufferBuilder.vertex(matrix,tpos2.x,tpos2.y,tpos2.z).color(color5.getARGB()).next();
        bufferBuilder.vertex(matrix,tpos4.x,tpos4.y,tpos4.z).color(color5.getARGB()).next();
        bufferBuilder.vertex(matrix,tpos3.x,tpos3.y,tpos3.z).color(color5.getARGB()).next();
//
        bufferBuilder.vertex(matrix,btpos1.x,btpos1.y,btpos1.z).color(color6.getARGB()).next();
        bufferBuilder.vertex(matrix,btpos2.x,btpos2.y,btpos2.z).color(color6.getARGB()).next();
        bufferBuilder.vertex(matrix,btpos4.x,btpos4.y,btpos4.z).color(color6.getARGB()).next();
        bufferBuilder.vertex(matrix,btpos3.x,btpos3.y,btpos3.z).color(color6.getARGB()).next();
        tessellator.draw();

        RenderSystem.setShaderTexture(0, imagePath);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        bufferBuilder.vertex(matrix,fpos1.x,fpos1.y,fpos1.z).texture(0, 0).color(colorImages ? color1.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,fpos2.x,fpos2.y,fpos2.z).texture(0, 1).color(colorImages ? color1.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,fpos4.x,fpos4.y,fpos4.z).texture(1, 1).color(colorImages ? color1.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,fpos3.x,fpos3.y,fpos3.z).texture(1, 0).color(colorImages ? color1.getARGB() : color6.getARGB()).next();


        bufferBuilder.vertex(matrix,lpos1.x,lpos1.y,lpos1.z).texture(0, 0).color(colorImages ? color2.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,lpos2.x,lpos2.y,lpos2.z).texture(0, 1).color(colorImages ? color2.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,lpos4.x,lpos4.y,lpos4.z).texture(1, 1).color(colorImages ? color2.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,lpos3.x,lpos3.y,lpos3.z).texture(1, 0).color(colorImages ? color2.getARGB() : color6.getARGB()).next();


        bufferBuilder.vertex(matrix,bpos1.x,bpos1.y,bpos1.z).texture(0, 0).color(colorImages ? color3.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,bpos2.x,bpos2.y,bpos2.z).texture(0, 1).color(colorImages ? color3.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,bpos4.x,bpos4.y,bpos4.z).texture(1, 1).color(colorImages ? color3.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,bpos3.x,bpos3.y,bpos3.z).texture(1, 0).color(colorImages ? color3.getARGB() : color6.getARGB()).next();


        bufferBuilder.vertex(matrix,rpos1.x,rpos1.y,rpos1.z).texture(0, 0).color(colorImages ? color4.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,rpos2.x,rpos2.y,rpos2.z).texture(0, 1).color(colorImages ? color4.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,rpos4.x,rpos4.y,rpos4.z).texture(1, 1).color(colorImages ? color4.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,rpos3.x,rpos3.y,rpos3.z).texture(1, 0).color(colorImages ? color4.getARGB() : color6.getARGB()).next();


        bufferBuilder.vertex(matrix,tpos1.x,tpos1.y,tpos1.z).texture(0, 0).color(colorImages ? color5.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,tpos2.x,tpos2.y,tpos2.z).texture(0, 1).color(colorImages ? color5.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,tpos4.x,tpos4.y,tpos4.z).texture(1, 1).color(colorImages ? color5.getARGB() : color6.getARGB()).next();
        bufferBuilder.vertex(matrix,tpos3.x,tpos3.y,tpos3.z).texture(1, 0).color(colorImages ? color5.getARGB() : color6.getARGB()).next();

//
        bufferBuilder.vertex(matrix,btpos1.x,btpos1.y,btpos1.z).texture(0, 0).color(color6.getARGB()).next();
        bufferBuilder.vertex(matrix,btpos2.x,btpos2.y,btpos2.z).texture(0, 1).color(color6.getARGB()).next();
        bufferBuilder.vertex(matrix,btpos4.x,btpos4.y,btpos4.z).texture(1, 1).color(color6.getARGB()).next();
        bufferBuilder.vertex(matrix,btpos3.x,btpos3.y,btpos3.z).texture(1, 0).color(color6.getARGB()).next();

        tessellator.draw();
        matrixStack.pop();
        //RenderSystem.enableDepthTest();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EVENT_MANAGER.subscribe(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EVENT_MANAGER.unsubscribe(this);
    }

    private boolean isInBoundsY(int y) {
        int height = MC.getWindow().getScaledHeight();
        return y > 0 && y < height;
    }

    private boolean isInBoundsX(int x) {
        int width = MC.getWindow().getScaledWidth();
        return x > 0 && x < width;
    }

    void checkedMove() {
        if (movingDown) {
            if (isInBoundsY(this.coordY + height + (int)stepsize)) {
                this.coordY += stepsize;
            } else {
                movingDown = !movingDown;
            }
        }
        if (!movingDown) {
            if (isInBoundsY(this.coordY - (int)stepsize)) {
                this.coordY -= stepsize;
            } else {
                movingDown = !movingDown;
            }
        }

        if (movingRight) {
            if (isInBoundsX(this.coordX + width + (int)stepsize)) {
                this.coordX += stepsize;
            } else {
                movingRight = !movingRight;
            }
        }
        if (!movingRight) {
            if (isInBoundsX(this.coordX - (int)stepsize)) {
                this.coordX -= stepsize;
            } else {
                movingRight = !movingRight;
            }
        }
    }
}