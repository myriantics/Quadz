package dev.lazurite.quadz.common.hooks;

import dev.lazurite.quadz.client.networking.QuadzClientPlayNetworking;
import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.common.networking.QuadzServerPlayNetworking;
import dev.lazurite.quadz.common.networking.c2s.JoystickInputC2SPacket;
import dev.lazurite.quadz.common.networking.s2c.JoystickInputS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerHooks {

    private static final Map<ResourceLocation, Float> joystickValues = new ConcurrentHashMap<>();

    public static float onGetJoystickValues(ResourceLocation axis) {
        return joystickValues.get(axis) == null ? 0.0f : joystickValues.get(axis);
    }

    public static Map<ResourceLocation, Float> onGetAllAxes() {
        return new HashMap<>(joystickValues);
    }

    public static void onSetJoystickValue(ResourceLocation axis, float value) {
        joystickValues.put(axis, value);
    }

    public static void onSyncJoystick(Player player) {
        Level level = player.level();
        Map<ResourceLocation, Float> joysticks = new HashMap<>(joystickValues);

        if (level.isClientSide()) {
            QuadzClientPlayNetworking.send(new JoystickInputC2SPacket(joysticks));
        } else {
            level.players().forEach(p -> {
                if (!p.equals(player) && p instanceof ServerPlayer serverPlayer) {
                    QuadzServerPlayNetworking.send(serverPlayer, new JoystickInputS2CPacket(serverPlayer.getId(), joysticks));
                }
            });
        }
    }

    /**
     * Prevents the camera from going back to the {@link ServerPlayer}
     * when they crouch at this particular instance of the game loop.
     */
    public static boolean onWantsToStopRiding(ServerPlayer player) {
        return !(player.getCamera() instanceof Quadcopter) && player.isCrouching();
    }

}
