package dev.lazurite.quadz.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.lazurite.quadz.extension.MinecraftExtension;
import dev.lazurite.quadz.render.RenderHooks;
import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
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

    @WrapMethod(
            method = "renderItemInHand"
    )
    private void renderItemInHand$HEAD(Camera camera, float f, Matrix4f matrix4f, Operation<Void> original) {
        if (!((MinecraftExtension) Minecraft.getInstance()).quadz$getQuadcopterInterface().isEnabled()) {
            original.call(camera, f, matrix4f);
        }
    }
}
