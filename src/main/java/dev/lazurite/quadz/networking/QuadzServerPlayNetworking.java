package dev.lazurite.quadz.networking;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.networking.c2s.ControllerInputC2SPacket;
import dev.lazurite.quadz.networking.c2s.RequestPlayerViewC2SPacket;
import dev.lazurite.quadz.networking.c2s.RequestQuadcopterViewC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class QuadzServerPlayNetworking {
    public static void send(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void initC2SRecievers() {
        ServerPlayNetworking.registerGlobalReceiver(RequestPlayerViewC2SPacket.TYPE, QuadzServerPlayNetworkHandler::onPlayerViewRequestReceived);
        ServerPlayNetworking.registerGlobalReceiver(ControllerInputC2SPacket.TYPE, QuadzServerPlayNetworkHandler::onControllerInput);
        ServerPlayNetworking.registerGlobalReceiver(RequestQuadcopterViewC2SPacket.TYPE, QuadzServerPlayNetworkHandler::onQuadcopterViewRequested);
        QuadzCommon.LOGGER.info("Initialized Quadz' C2S Packet Recievers!");
    }
}
