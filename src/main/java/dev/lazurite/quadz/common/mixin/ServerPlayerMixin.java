package dev.lazurite.quadz.common.mixin;

import com.mojang.authlib.GameProfile;
import dev.lazurite.quadz.common.hooks.PlayerHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;wantsToStopRiding()Z"
            )
    )
    public boolean tick$wantsToStopRiding(ServerPlayer player) {
        return PlayerHooks.onWantsToStopRiding(player);
    }

}
