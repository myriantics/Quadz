package dev.lazurite.quadz.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.lazurite.quadz.client.extension.LocalPlayerExtension;
import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.item.RemoteItem;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements LocalPlayerExtension {

    @Unique
    private @Nullable Quadcopter quadz$activeQuadcopter = null;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Override
    public @Nullable Quadcopter quadz$getActiveQuadcopter() {
        return this.quadz$activeQuadcopter;
    }

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/AmbientSoundHandler;tick()V")
    )
    private void quadz$updateActiveQuadcopter(CallbackInfo ci) {
        ItemStack mainHandStack = this.getMainHandItem();

        if (mainHandStack.getItem() instanceof RemoteItem && mainHandStack.has(QuadzDataComponentTypes.BINDING)) {

            @Nullable UUID boundUUID = mainHandStack.get(QuadzDataComponentTypes.BINDING).boundUUID();
            if (boundUUID != null && !(this.quadz$activeQuadcopter != null && this.quadz$activeQuadcopter.getUUID().equals(boundUUID))) {
                Vec3 eyePos = this.getEyePosition();
                final double range = 256;

                this.quadz$activeQuadcopter = this.level().getNearestEntity(
                        Quadcopter.class,
                        TargetingConditions.DEFAULT.selector((livingEntity -> livingEntity.getUUID().equals(boundUUID))),
                        null,
                        eyePos.x, eyePos.y, eyePos.z,
                        AABB.unitCubeFromLowerCorner(eyePos).inflate(range)
                );
            }
        }
    }
}
