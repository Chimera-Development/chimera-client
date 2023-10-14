package dev.chimera.client.util;

import net.minecraft.text.Text;

public class ModuleCategories {
    public static final TabTree MISC = new TabTree(Text.literal("Miscellaneous"));
    public static final TabTree COMBAT = new TabTree(Text.literal("Combat"));
    public static final TabTree WORLD = new TabTree(Text.literal("World"));
    public static final TabTree RENDER = new TabTree(Text.literal("Render"));
    public static final TabTree MOVEMENT = new TabTree(Text.literal("Movement"));
    public static final TabTree PLAYER = new TabTree(Text.literal("Player"));
}
