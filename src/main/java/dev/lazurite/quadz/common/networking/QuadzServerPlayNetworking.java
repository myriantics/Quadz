package dev.lazurite.quadz.common.networking;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.networking.c2s.JoystickInputC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestPlayerViewC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestQuadcopterViewC2SPacket;
import dev.lazurite.quadz.common.networking.s2c.JoystickInputS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class QuadzServerPlayNetworking {
    public static void send(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void initC2SRecievers() {
        ServerPlayNetworking.registerGlobalReceiver(JoystickInputC2SPacket.TYPE, QuadzServerPlayNetworkHandler::onJoystickInput);
        ServerPlayNetworking.registerGlobalReceiver(RequestPlayerViewC2SPacket.TYPE, QuadzServerPlayNetworkHandler::onPlayerViewRequestReceived);
        ServerPlayNetworking.registerGlobalReceiver(RequestQuadcopterViewC2SPacket.TYPE, QuadzServerPlayNetworkHandler::onQuadcopterViewRequested);
        QuadzCommon.LOGGER.info("Initialized Quadz' C2S Packet Recievers!");
    }
}
