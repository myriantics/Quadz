package dev.lazurite.quadz.networking;

import dev.lazurite.quadz.QuadzCommon;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public abstract class QuadzClientPlayNetworking {

    public static void send(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public static void initS2CRecievers() {
        QuadzCommon.LOGGER.info("Initialized Quadz' S2C Packet Recievers!");
    }
}
