package dev.lazurite.quadz.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.lazurite.quadz.client.render.RenderHooks;
import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void renderLevel$HEAD(DeltaTracker deltaTracker, CallbackInfo ci) {
        RenderHooks.onRenderLevel(deltaTracker.getGameTimeDeltaPartialTick(true));
    }

    @ModifyArg(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;conjugate(Lorg/joml/Quaternionf;)Lorg/joml/Quaternionf;"
            )
    )
    public Quaternionf renderLevel$multiplyYaw(Quaternionf quaternion) {
        return RenderHooks.onMultiplyYaw(quaternion);
    }

    @ModifyArg(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;conjugate(Lorg/joml/Quaternionf;)Lorg/joml/Quaternionf;"
            )
    )
    public Quaternionf renderLevel$multiplyPitch(Quaternionf quaternion) {
        return RenderHooks.onMultiplyPitch(quaternion);
    }

    @WrapMethod(
            method = "renderItemInHand"
    )
    private void renderItemInHand$HEAD(Camera camera, float f, Matrix4f matrix4f, Operation<Void> original) {
        if (!(camera.getEntity() instanceof Quadcopter)) {
            original.call(camera, f, matrix4f);
        }
    }

}
