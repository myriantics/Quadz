package dev.lazurite.quadz.render;

import com.google.common.collect.Maps;
import com.mojang.math.Axis;
import dev.lazurite.quadz.control.QuadcopterInterface;
import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.extension.MinecraftExtension;
import dev.lazurite.quadz.registry.QuadzEvents;
import dev.lazurite.quadz.util.JoystickOutput;
import dev.lazurite.quadz.Config;
import dev.lazurite.quadz.QuadzClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class RenderHooks {

    public static void onRenderLevel(float tickDelta) {
        /* Rotate the player's yaw and pitch to follow the quadcopter */
        LocalPlayer player = Minecraft.getInstance().player;
        QuadcopterInterface quadcopterInterface = ((MinecraftExtension)Minecraft.getInstance()).quadz$getQuadcopterInterface();

        if (Config.followLOS && quadcopterInterface.isEnabled()) {
            @Nullable Quadcopter activeQuadcopter = player.quadz$getActiveQuadcopter();
            // don't look at the quadcopter if it's the current camera
            if (Minecraft.getInstance().getCameraEntity() != activeQuadcopter) {
                if (player.hasLineOfSight(activeQuadcopter)) {
                    /* Get the difference in position between the player and the quadcopter */
                    var delta = Minecraft.getInstance().player.getEyePosition(tickDelta)
                            .subtract(activeQuadcopter.getPosition(tickDelta));

                    /* Set new pitch and yaw */
                    player.setYRot((float) Math.toDegrees(Math.atan2(delta.z, delta.x)) + 90);
                    player.setXRot(20 + (float) Math.toDegrees(Math.atan2(delta.y, Math.sqrt(Math.pow(delta.x, 2) + Math.pow(delta.z, 2)))));
                }
            }
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
