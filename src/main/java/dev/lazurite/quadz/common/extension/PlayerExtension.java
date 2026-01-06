package dev.lazurite.quadz.common.extension;

import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface PlayerExtension {
    default @Nullable Quadcopter quadz$getActiveQuadcopter() {
        return null;
    }
}
