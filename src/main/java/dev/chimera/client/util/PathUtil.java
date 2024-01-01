package dev.chimera.client.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    public static Path getFullPath(Identifier identifier) {
        File gameDir = FabricLoader.getInstance().getGameDirectory();
        Path assetPath = gameDir.toPath().resolve("assets").resolve(identifier.getNamespace()).resolve(identifier.getPath());
        return assetPath.toAbsolutePath();
    }

}
