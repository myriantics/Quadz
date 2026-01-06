package dev.lazurite.quadz.mixin.minecraft;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Inject(
            method = "tick",
            at = @At(value = "TAIL")
    )
    private void fart(CallbackInfo ci) {

    }
}
