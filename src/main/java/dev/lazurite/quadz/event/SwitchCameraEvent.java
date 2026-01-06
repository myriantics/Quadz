package dev.lazurite.quadz.event;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface SwitchCameraEvent {
    void onSwitchCamera(Entity previousCameraEntity, Entity currentCameraEntity);
}
