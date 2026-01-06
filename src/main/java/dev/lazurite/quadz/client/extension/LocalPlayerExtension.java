package dev.lazurite.quadz.client.extension;

import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface LocalPlayerExtension {
    default Map<ResourceLocation, Float> quadz$getJoystickInputs() {
        return Map.of();
    }

}
