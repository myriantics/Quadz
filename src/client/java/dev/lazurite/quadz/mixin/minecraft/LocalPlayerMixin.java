package dev.lazurite.quadz.mixin.minecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.lazurite.quadz.extension.MinecraftExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @ModifyReturnValue(
            method = "isControlledCamera",
            at = @At(value = "RETURN")
    )
    private boolean quadz$cancelPlayerMovementWhenInterfacing(boolean original) {
        return original && !((MinecraftExtension) Minecraft.getInstance()).quadz$getQuadcopterInterface().isEnabled();
    }
}
