package dev.lazurite.quadz.client.render;

import com.google.common.collect.Maps;
import com.mojang.math.Axis;
import dev.lazurite.quadz.common.registry.QuadzEvents;
import dev.lazurite.quadz.common.util.JoystickOutput;
import dev.lazurite.quadz.client.Config;
import dev.lazurite.quadz.client.QuadzClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Quaternionf;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class RenderHooks {

    public static void onRenderLevel(float tickDelta) {
        /* Rotate the player's yaw and pitch to follow the quadcopter */
        var player = Minecraft.getInstance().player;

        if (Config.followLOS && player != null) {
            QuadzClient.getQuadcopterFromRemote().ifPresent(quadcopter -> {
                if (player.hasLineOfSight(quadcopter)) {
                    /* Get the difference in position between the player and the quadcopter */
                    var delta = Minecraft.getInstance().player.getEyePosition(tickDelta)
                            .subtract(quadcopter.getPosition(tickDelta));

                    /* Set new pitch and yaw */
					player.setYRot((float) Math.toDegrees(Math.atan2(delta.z, delta.x)) + 90);
					player.setXRot(20 + (float) Math.toDegrees(Math.atan2(delta.y, Math.sqrt(Math.pow(delta.x, 2) + Math.pow(delta.z, 2)))));
                }
            });
        }
    }

    private static boolean loaded;
    private static long next;

    public static void onRenderMinecraft(ProfilerFiller profiler) {
        profiler.popPush("gamepadInput");

        if (System.currentTimeMillis() > next) {
            Map<Integer, String> lastJoysticks = Maps.newHashMap(JoystickOutput.JOYSTICKS);
            JoystickOutput.JOYSTICKS.clear();

            for (int i = 0; i < 16; i++) {
                if (glfwJoystickPresent(i)) {
                    JoystickOutput.JOYSTICKS.put(i, JoystickOutput.getJoystickName(i));

                    if (!lastJoysticks.containsKey(i) && loaded) {
                        QuadzEvents.JOYSTICK_CONNECT.invoker().onConnect(i, JoystickOutput.getJoystickName(i));
                    }
                } else if (lastJoysticks.containsKey(i) && loaded) {
                    QuadzEvents.JOYSTICK_DISCONNECT.invoker().onDisconnect(i, lastJoysticks.get(i));
                }
            }

            next = System.currentTimeMillis() + 500;
            loaded = true;
        }
    }

    public static Quaternionf onMultiplyYaw(Quaternionf quaternion) {
        if (QuadzClient.getQuadcopterFromCamera().isPresent()) {
            return Axis.YP.rotationDegrees(180f);
        }

        return quaternion;
    }

    public static Quaternionf onMultiplyPitch(Quaternionf quaternion) {
        if (QuadzClient.getQuadcopterFromCamera().isPresent()) {
            return new Quaternionf(0, 0, 0, 1);
        }

        return quaternion;
    }

}
