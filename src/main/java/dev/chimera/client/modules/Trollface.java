package dev.chimera.client.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.events.RenderEvent;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static dev.chimera.client.ChimeraClient.*;

@Listener(references = References.Strong)
public class Trollface extends AbstractModule {

    private int coordX = 10;
    private int coordY = 10;

    private boolean movingDown = false;
    private boolean movingRight = false;

    private MinecraftClient MC = MinecraftClient.getInstance();
    Identifier imagePath = new Identifier(MOD_ID, "trollface.png");

    public Trollface() {
        super("Trollface", "Displays a bouncing trollface on your screen", GLFW.GLFW_KEY_G);
        this.onEnable();
    }

    public Trollface(Identifier imagePath) {
        super("Trollface", "Displays a bouncing trollface on your screen", GLFW.GLFW_KEY_G);
        this.onEnable();
        this.imagePath = imagePath;
    }


    @Handler
    public void render(RenderEvent event) {
        RenderSystem.disableDepthTest();

        checkedMove();
        event.drawContext.drawTexture(imagePath, coordX, coordY, 0, 0, 256, 256);

        RenderSystem.enableDepthTest();
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
        if (y > 0 && y < height) {
            return true;
        }
        return false;
    }

    private boolean isInBoundsX(int x) {
        int width = MC.getWindow().getScaledWidth();
        if (x > 0 && x < width) {
            return true;
        }
        return false;
    }

    void checkedMove() {
        if (movingDown) {
            if (isInBoundsY(this.coordY + 256 + 1)) {
                this.coordY++;
            } else {
                movingDown = !movingDown;
            }
        }
        if (!movingDown) {
            if (isInBoundsY(this.coordY - 1)) {
                this.coordY--;
            } else {
                movingDown = !movingDown;
            }
        }

        if (movingRight) {
            if (isInBoundsX(this.coordX + 256 + 1)) {
                this.coordX++;
            } else {
                movingRight = !movingRight;
            }
        }
        if (!movingRight) {
            if (isInBoundsX(this.coordX - 1)) {
                this.coordX--;
            } else {
                movingRight = !movingRight;
            }
        }
    }
}