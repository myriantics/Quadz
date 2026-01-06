package dev.lazurite.quadz.client.mixin;

import dev.lazurite.quadz.client.QuadzClient;
import dev.lazurite.quadz.client.render.RenderHooks;
import dev.lazurite.quadz.common.entity.Quadcopter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow private ProfilerFiller profiler;

    @Inject(
            method = "runTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(Lnet/minecraft/client/DeltaTracker;Z)V")
    )
    public void runTick$render(boolean bl, CallbackInfo ci) {
        RenderHooks.onRenderMinecraft(this.profiler);
    }
}
