package dev.lazurite.quadz.client.networking;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.networking.s2c.JoystickInputS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class QuadzClientPlayNetworking {

    public static void send(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public static void initS2CRecievers() {
        ClientPlayNetworking.registerGlobalReceiver(JoystickInputS2CPacket.TYPE, QuadzClientPlayNetworkHandler::onJoystickInput);
        QuadzCommon.LOGGER.info("Initialized Quadz' S2C Packet Recievers!");
    }
}
