package dev.lazurite.quadz.event;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.QuadzClient;
import dev.lazurite.quadz.networking.QuadzClientPlayNetworking;
import dev.lazurite.quadz.networking.c2s.RequestQuadcopterViewC2SPacket;
import dev.lazurite.quadz.util.JoystickOutput;
import dev.lazurite.quadz.Config;
import dev.lazurite.quadz.render.screen.ControllerConnectedToast;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

public class ClientEventHooks {

    public static void onClientTick(Minecraft minecraft) {
        if (!minecraft.isPaused() && minecraft.player != null && JoystickOutput.controllerExists()) {
            JoystickOutput.getAxisValue(minecraft.player, Config.pitch, QuadzCommon.locate("pitch"), Config.pitchInverted, false);
            JoystickOutput.getAxisValue(minecraft.player, Config.yaw, QuadzCommon.locate("yaw"), Config.yawInverted, false);
            JoystickOutput.getAxisValue(minecraft.player, Config.roll, QuadzCommon.locate("roll"), Config.rollInverted, false);
            JoystickOutput.getAxisValue(minecraft.player, Config.throttle, QuadzCommon.locate("throttle"), Config.throttleInverted, Config.throttleInCenter);
        }
    }

    public static void onJoystickConnect(int id, String name) {
        ControllerConnectedToast.add(Component.translatable("quadz.toast.connect"), name);
    }

    public static void onJoystickDisconnect(int id, String name) {
        ControllerConnectedToast.add(Component.translatable("quadz.toast.disconnect"), name);
    }

    public static void onLeftClick() {
        QuadzClientPlayNetworking.send(RequestQuadcopterViewC2SPacket.LEFT_CLICK);
    }

    public static void onRightClick() {
        QuadzClientPlayNetworking.send(RequestQuadcopterViewC2SPacket.RIGHT_CLICK);
    }

    public static void onClientLevelTick(ClientLevel level) {
        final var client = Minecraft.getInstance();

        if (!client.isPaused()) {
            if (client.options.keyShift.isDown() && QuadzClient.getQuadcopterFromCamera().isPresent()) {
                client.options.getCameraType().quadz$reset();
            }
        }
    }

    public static void onPostLogin(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft minecraft) {
        if (minecraft.player != null) {
            //minecraft.player.quadz$setJoystickValue(QuadzCommon.locate("rate"), dev.lazurite.quadz.Config.rate);
            //minecraft.player.quadz$setJoystickValue(QuadzCommon.locate("super_rate"), dev.lazurite.quadz.Config.superRate);
            //minecraft.player.quadz$setJoystickValue(QuadzCommon.locate("expo"), dev.lazurite.quadz.Config.expo);
        }
    }
}
