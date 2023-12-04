package dev.chimera.client.util;

public class MathUtil {

    public static double round(float value, int places){
        double scale = Math.pow(10.0,places);
        return Math.round(value * scale) / scale;
    }
    public static double round(double value, int places){
        double scale = Math.pow(10.0,places);
        return Math.round(value * scale) / scale;
    }
}
