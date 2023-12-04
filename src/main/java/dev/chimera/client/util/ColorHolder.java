package dev.chimera.client.util;

import lombok.Getter;


import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4f;

public class ColorHolder {
    private int r;

    private int g;

    private int b;

    private int a;

    public ColorHolder(){
        r = 255;
        g = 255;
        b = 255;
        a = 255;
    }
    public ColorHolder(int argb){
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8) & 0xFF;
        this.b = argb & 0xFF;
    }
    public ColorHolder(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setGLColor() {
        glColor4f(this.r / 255f, this.g / 255f, this.b / 255f, this.a / 255f);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public static ColorHolder random(){
        return new ColorHolder((int) (Math.random() * 255),(int)(Math.random()*255),(int)(Math.random()*255),255);
    }

    public static ColorHolder from(Color color){
        return new ColorHolder(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
    }
    public int getARGB() {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
