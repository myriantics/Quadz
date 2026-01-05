package dev.lazurite.quadz.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class QuadzClientPlayNetworking {

    public static void send(ResourceLocation id, Consumer<FriendlyByteBuf> bufEdits) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        bufEdits.accept(buf);
        ClientPlayNetworking.send(id, buf);
    }
}
