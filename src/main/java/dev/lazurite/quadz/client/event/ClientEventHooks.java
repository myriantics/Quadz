package dev.lazurite.quadz.client.event;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.client.QuadzClient;
import dev.lazurite.quadz.client.networking.QuadzClientPlayNetworkHandler;
import dev.lazurite.quadz.client.networking.QuadzClientPlayNetworking;
import dev.lazurite.quadz.common.networking.c2s.RequestQuadcopterViewC2SPacket;
import dev.lazurite.quadz.common.registry.QuadzPackets;
import dev.lazurite.quadz.common.util.JoystickOutput;
import dev.lazurite.quadz.client.Config;
import dev.lazurite.quadz.client.render.screen.ControllerConnectedToast;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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
            //minecraft.player.quadz$setJoystickValue(QuadzCommon.locate("rate"), Config.rate);
            //minecraft.player.quadz$setJoystickValue(QuadzCommon.locate("super_rate"), Config.superRate);
            //minecraft.player.quadz$setJoystickValue(QuadzCommon.locate("expo"), Config.expo);
        }
    }
}
