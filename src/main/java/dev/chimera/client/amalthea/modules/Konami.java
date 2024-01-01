package dev.chimera.client.amalthea.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.ChimeraClient;
import dev.chimera.client.events.KeyEvent;
import dev.chimera.client.events.RenderEvent;
import dev.chimera.client.modules.Module;
import dev.chimera.client.modules.ModuleCategory;
import dev.chimera.client.modules.ModuleManager;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.MusicSound;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.jetbrains.annotations.Nullable;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Time;
import java.util.Calendar;
import java.util.Objects;

import static dev.chimera.client.ChimeraClient.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glPushAttrib;

@Listener(references = References.Strong)
public class Konami extends Module {
    private int combo = 0;
    private boolean triggered = false;
    public Konami(){
        super("Konami","psst secret", ModuleCategory.GUI);
        this.onEnable();
        ChimeraClient.LOGGER.info("Enabled the secret 🤫");
    }

    @Handler
    public void onKeyPress(KeyEvent event){
        System.out.println("key event");
        if (combo == 0 && event.key == GLFW_KEY_UP){
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 1 && event.key == GLFW_KEY_UP) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 2 && event.key == GLFW_KEY_DOWN) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 3 && event.key == GLFW_KEY_DOWN) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 4 && event.key == GLFW_KEY_LEFT) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 5 && event.key == GLFW_KEY_RIGHT) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 6 && event.key == GLFW_KEY_LEFT) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 7 && event.key == GLFW_KEY_RIGHT) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 8 && event.key == GLFW_KEY_B) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 9 && event.key == GLFW_KEY_A) {
            combo++;
            System.out.println(String.format("combo++, combo: %d",combo));
        } else if (combo == 10 && event.key == GLFW_KEY_BACKSPACE){
            combo = 0;
            triggered = true;
            System.out.println(String.format("code triggered, combo: %d",combo));
        } else {
            combo = 0;
            triggered = false;
            System.out.println("fucked up code");
        }

    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    @Handler
    public void onRender(RenderEvent event){
        if (triggered) {
            ModuleManager.getModule(Maxwell.class).setActive(false);
        }
    }
    public static class SecretScreen extends Screen {
        File content;
        FFmpegFrameGrabber grabber;
        Java2DFrameConverter c;
        int counter = 0;
        public SecretScreen() {
            super(Text.of("Super Secret"));
            File modDirectory = FabricLoader.getInstance().getModContainer(MOD_ID).get().getRootPath().toFile();
            content =  new File(modDirectory, "assets/chimera-client/content.mp4");
            grabber = new FFmpegFrameGrabber(content.getAbsoluteFile());
            VideoCapture videoCapture = new VideoCapture(0);
            c = new Java2DFrameConverter();
            try {
                grabber.start();
            } catch (FFmpegFrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }

        }
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta){
            NativeImageBackedTexture image = null;
            try {
                Frame f = grabber.grabImage();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                BufferedImage bmg  = c.getBufferedImage(f);
                if (bmg != null){
                    ImageIO.write(bmg,"png",os);
                }
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                image =  new NativeImageBackedTexture(NativeImage.read(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (image == null){
                try {
                    grabber.stop();

                } catch (FFmpegFrameGrabber.Exception e) {
                    throw new RuntimeException(e);
                }
            }
            this.renderBackground(context,mouseX,mouseY,delta);
            Identifier frame = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID,image);
            int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
            int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
            RenderSystem.limitDisplayFPS(43);
            context.drawTexture(frame,0,0,0,0, width,height,width,height);
            counter++;
        }

        //@Nullable
        @Override
        public MusicSound getMusic() {
            return secretSound;
        }
    }
}
