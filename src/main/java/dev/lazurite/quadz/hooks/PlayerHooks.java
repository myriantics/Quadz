package dev.lazurite.quadz.hooks;

import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.networking.QuadzServerPlayNetworking;
import dev.lazurite.quadz.networking.c2s.JoystickInputC2SPacket;
import dev.lazurite.quadz.networking.s2c.JoystickInputS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerHooks {

    /**
     * Prevents the camera from going back to the {@link ServerPlayer}
     * when they crouch at this particular instance of the game loop.
     */
    public static boolean onWantsToStopRiding(ServerPlayer player) {
        return !(player.getCamera() instanceof Quadcopter) && player.isCrouching();
    }

}
