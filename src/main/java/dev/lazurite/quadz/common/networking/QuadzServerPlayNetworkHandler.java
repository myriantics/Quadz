package dev.lazurite.quadz.common.networking;

import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.registry.QuadzEvents;
import dev.lazurite.quadz.common.util.Bindable;
import dev.lazurite.quadz.common.util.Search;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.ArrayList;
import java.util.Optional;

public abstract class QuadzServerPlayNetworkHandler {

    public static void onJoystickInput(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        int axisCount = buf.readInt();

        for (int i = 0; i < axisCount; i++) {
            ResourceLocation axis = buf.readResourceLocation();
            float value = buf.readFloat();
            serverPlayer.quadz$setJoystickValue(axis, value);
        }
    }

    public static void onPlayerViewRequestReceived(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        Optional.ofNullable(serverPlayer.getServer()).ifPresent(server -> server.execute(() -> serverPlayer.setCamera(serverPlayer)));
    }

    public static void onQuadcopterViewRequested(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf buf, PacketSender packetSender) {
        int spectateDirection = buf.readInt();

        Optional.ofNullable(serverPlayer.getServer()).ifPresent(server -> {
            server.execute(() -> {
                if (serverPlayer.getCamera() instanceof Quadcopter quadcopter) {
                    var allQuadcopters = new ArrayList<>(Search.forAllViewed(server));
                    var index = Math.max(allQuadcopters.lastIndexOf(quadcopter) + spectateDirection, 0);
                    var entity = allQuadcopters.get(index % allQuadcopters.size());
                    serverPlayer.setCamera(entity);
                    QuadzEvents.SWITCH_CAMERA_EVENT.invoker().onSwitchCamera(serverPlayer.getCamera(), entity);
                } else {
                    Bindable.get(serverPlayer.getMainHandItem()).ifPresent(bindable -> {
                        Search.forQuadWithBindId(
                                        serverPlayer.getLevel(),
                                        serverPlayer.getCamera().position(),
                                        bindable.getBindId(),
                                        server.getPlayerList().getViewDistance() * 16)
                                .ifPresentOrElse(entity -> {
                                    serverPlayer.setCamera(entity);
                                    QuadzEvents.SWITCH_CAMERA_EVENT.invoker().onSwitchCamera(serverPlayer.getCamera(), entity);
                                }, () -> Search.forAllViewed(server).stream().findFirst().ifPresent(entity -> {
                                    serverPlayer.setCamera(entity);
                                    QuadzEvents.SWITCH_CAMERA_EVENT.invoker().onSwitchCamera(serverPlayer.getCamera(), entity);
                                }));
                    });

                }
            });
        });
    }
}
