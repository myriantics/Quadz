package dev.lazurite.quadz.client.networking;

import dev.lazurite.quadz.common.networking.s2c.JoystickInputS2CPacket;
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
    public static void onJoystickInput(JoystickInputS2CPacket payload, ClientPlayNetworking.Context context) {
        if (context.client().level != null && context.client().player == context.client().level.getEntity(payload.playerId())) {
            for (ResourceLocation id : payload.values().keySet()) {
                context.player().quadz$setJoystickValue(id, payload.values().get(id));
            }
        }
    }
}
