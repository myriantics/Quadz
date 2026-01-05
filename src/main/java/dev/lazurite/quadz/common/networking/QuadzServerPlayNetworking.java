package dev.lazurite.quadz.common.networking;

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
        FriendlyByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send(player, payload);
    }
}
