package dev.lazurite.quadz.common.extension;

import dev.lazurite.quadz.common.entity.Quadcopter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface PlayerExtension {

    default float quadz$getJoystickValue(ResourceLocation axis) {
        return 0.0f;
    }

    default void quadz$setJoystickValue(ResourceLocation axis, float value) {
    }

    default Map<ResourceLocation, Float> quadz$getAllAxes() {
        return new HashMap<>();
    }

    default void quadz$syncJoystick() {
    }

}
