package dev.lazurite.quadz.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class QuadzClientPlayNetworkHandler {
    public static void onJoystickInput(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        UUID id = buf.readUUID();
        int axisCount = buf.readInt();
        Player player = Minecraft.getInstance().level.getPlayerByUUID(id);

        if (player != null) {
            for (int i = 0; i < axisCount; i++) {
                ResourceLocation axis = buf.readResourceLocation();
                float value = buf.readFloat();
                player.quadz$setJoystickValue(axis, value);
            }
        }
    }

}
