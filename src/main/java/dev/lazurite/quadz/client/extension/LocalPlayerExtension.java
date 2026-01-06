package dev.lazurite.quadz.client.extension;

import dev.lazurite.quadz.common.entity.Quadcopter;
import org.jetbrains.annotations.Nullable;

public interface LocalPlayerExtension {
    default @Nullable Quadcopter quadz$getActiveQuadcopter() {
        return null;
    }
}
