package dev.lazurite.quadz.extension;

import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import org.jetbrains.annotations.Nullable;

public interface PlayerExtension {
    default @Nullable Quadcopter quadz$getActiveQuadcopter() {
        return null;
    }
}
