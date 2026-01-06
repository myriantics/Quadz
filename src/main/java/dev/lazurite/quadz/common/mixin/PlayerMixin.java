package dev.lazurite.quadz.common.mixin;

import com.mojang.authlib.GameProfile;
import dev.lazurite.quadz.common.component.BindingComponent;
import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.common.extension.PlayerExtension;
import dev.lazurite.quadz.common.hooks.PlayerHooks;
import dev.lazurite.quadz.common.item.RemoteItem;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerExtension {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private @Nullable Quadcopter quadz$activeQuadcopter = null;

    @Override
    public @Nullable Quadcopter quadz$getActiveQuadcopter() {
        return this.quadz$activeQuadcopter;
    }

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tick()V")
    )
    private void quadz$updateActiveQuadcopter(CallbackInfo ci) {
        ItemStack mainHandStack = this.getMainHandItem();

        // remove it if its discarded
        if (this.quadz$activeQuadcopter != null && this.quadz$activeQuadcopter.isRemoved()) {
            this.quadz$activeQuadcopter = null;
        }

        if (mainHandStack.getItem() instanceof RemoteItem) {

            @Nullable UUID boundUUID = mainHandStack.getOrDefault(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND).boundUUID();
            if (boundUUID != null) {
                if (!(this.quadz$activeQuadcopter != null && this.quadz$activeQuadcopter.getUUID().equals(boundUUID))) {
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
            } else {
                // if the selected remote doesn't have a uuid, clear cache
                this.quadz$activeQuadcopter = null;
            }
        }
    }
}
