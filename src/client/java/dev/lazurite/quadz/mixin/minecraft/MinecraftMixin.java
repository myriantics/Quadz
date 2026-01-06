package dev.lazurite.quadz.mixin.minecraft;

import dev.lazurite.quadz.control.ControllerSim;
import dev.lazurite.quadz.control.QuadcopterInterface;
import dev.lazurite.quadz.extension.MinecraftExtension;
import dev.lazurite.quadz.render.RenderHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements MinecraftExtension {

    @Unique
    private final QuadcopterInterface quadz$quadcopterInterface = new QuadcopterInterface();

    @Shadow private ProfilerFiller profiler;

    @Override
    public QuadcopterInterface quadz$getQuadcopterInterface() {
        return this.quadz$quadcopterInterface;
    }

    @Inject(
            method = "runTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(Lnet/minecraft/client/DeltaTracker;Z)V")
    )
    public void runTick$render(boolean bl, CallbackInfo ci) {
        RenderHooks.onRenderMinecraft(this.profiler);
    }

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;tick(Ljava/util/function/BooleanSupplier;)V")
    )
    private void quadz$tickControllerSim(CallbackInfo ci) {
        if (this.quadz$quadcopterInterface.isEnabled()) {
            this.quadz$quadcopterInterface.tick();
        }
    }
}
