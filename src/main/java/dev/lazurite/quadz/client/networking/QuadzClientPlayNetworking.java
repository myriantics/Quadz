package dev.lazurite.quadz.client.networking;

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
}
