package dev.chimera.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.util.Pair;
import org.joml.Vector2d;

public class RenderUtil {
    public static void drawFilledRectangle(double x, double y, double width, double height, ColorHolder color){
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(255,255,255,255);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(x,y,0).color(color.getARGB()).next();
        bufferBuilder.vertex(x,y+height,0).color(color.getARGB()).next();
        bufferBuilder.vertex(x+width,y+height,0).color(color.getARGB()).next();
        bufferBuilder.vertex(x+width,y,0).color(color.getARGB()).next();
        tessellator.draw();
    }
    public static void drawFilledRectangle(Vector2d posStart,Vector2d posEnd,ColorHolder color){
        drawFilledRectangle(posStart.x,posStart.y,(posEnd.x-posStart.x),(posEnd.y-posStart.y),color);
    }
    public static void drawFilledRoundedRectangle(int x,int y,int width, int height, double radius, int segments, ColorHolder color){
        Vector2d posBegin = new Vector2d(x,y);
        Vector2d posEnd = new Vector2d(x+width,y+height);
        Vector2d pos2 = new Vector2d(posEnd.x, posBegin.y);
        Vector2d pos4 = new Vector2d(posBegin.x,posEnd.y);
        //radius = (width / 100) * radius;

        drawArcFilled(posBegin.add(radius,radius), radius, new Pair(-90f, 0f), segments, color); // Top left
        drawArcFilled(pos2.add(-radius, radius), radius, new Pair(0f, 90f), segments, color); // Top right
        drawArcFilled(posEnd.sub(radius,radius), radius, new Pair(90f, 180f), segments, color); // Bottom right
        drawArcFilled(pos4.add(radius, -radius), radius, new Pair(180f, 270f), segments, color); // Bottom left

        drawFilledRectangle(x+radius,y,width-radius*2,height,color);
        drawFilledRectangle(x,y+radius,width,height-radius*2,color);
    }
    public static void drawArcFilled(Vector2d center, double radius, Pair<Float,Float> angleRange, int segments, ColorHolder color) {
        Vector2d[] arcVertices = getArcVertices(center, radius, angleRange, segments);
        //System.out.println(center.toString());
        //System.out.println(Arrays.toString(Arrays.stream(arcVertices).toArray()));
        drawTriangleFan(center, arcVertices, color);
    }
    private static void drawTriangleFan(Vector2d center,Vector2d[] vertices, ColorHolder color){
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(255,255,255,255);
        RenderSystem.disableCull();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN,VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(center.x,center.y,0).color(color.getARGB()).next();

        for (Vector2d vertex : vertices) {
            bufferBuilder.vertex(vertex.x,vertex.y,0).color(color.getARGB()).next();
        }

        tessellator.draw();

    }
    private static Vector2d[] getArcVertices(Vector2d center, double radius, Pair<Float,Float> angleRange, int segments){
        Vector2d[] vertices = new Vector2d[segments+1];

        float range = Math.max(angleRange.getLeft(), angleRange.getRight()) - Math.min(angleRange.getLeft(),angleRange.getRight());
        int seg = calcSegments(segments,radius,range);
        float segAngle = (range)/ seg;

        for (int i = 0; i < vertices.length; i++) {
            double angle = Math.toRadians(i*segAngle + angleRange.getLeft());
            Vector2d unRounded = new Vector2d(Math.sin(angle),-Math.cos(angle)).mul(radius).add(center);
            vertices[i] = new Vector2d(MathUtil.round(unRounded.x,8),MathUtil.round(unRounded.y,8));
        }

        return vertices;
    }
    private static int calcSegments(int segmentsIn, double radius, float range){
        if (segmentsIn != -0) return segmentsIn;
        double segments = radius * 0.5 * Math.PI * (range/360f);
        return Math.max((int)Math.round(segments),16);
    }

}
