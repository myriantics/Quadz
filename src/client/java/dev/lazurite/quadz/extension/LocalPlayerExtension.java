package dev.lazurite.quadz.extension;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface LocalPlayerExtension {
    default Map<ResourceLocation, Float> quadz$getJoystickInputs() {
        return Map.of();
    }

}
