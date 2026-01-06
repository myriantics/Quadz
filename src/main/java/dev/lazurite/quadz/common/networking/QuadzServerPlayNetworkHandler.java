package dev.lazurite.quadz.common.networking;

import dev.lazurite.quadz.common.component.BindingComponent;
import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.networking.c2s.JoystickInputC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestPlayerViewC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestQuadcopterViewC2SPacket;
import dev.lazurite.quadz.common.registry.QuadzEvents;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import dev.lazurite.quadz.common.util.Search;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public abstract class QuadzServerPlayNetworkHandler {

    public static void onJoystickInput(JoystickInputC2SPacket packet, ServerPlayNetworking.Context context) {
        for (ResourceLocation id : packet.values().keySet()) {
            context.player().quadz$setJoystickValue(id, packet.values().get(id));
        }
    }

    public static void onPlayerViewRequestReceived(RequestPlayerViewC2SPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer serverPlayer = context.player();
        Optional.ofNullable(serverPlayer.getServer()).ifPresent(server -> server.execute(() -> serverPlayer.setCamera(serverPlayer)));
    }

    public static void onQuadcopterViewRequested(RequestQuadcopterViewC2SPacket packet, ServerPlayNetworking.Context context) {
        int spectateDirection = packet.clickType();
        ServerPlayer serverPlayer = context.player();

        Optional.ofNullable(serverPlayer.getServer()).ifPresent(server -> {
            server.execute(() -> {
                if (serverPlayer.getCamera() instanceof Quadcopter quadcopter) {
                    var allQuadcopters = new ArrayList<>(Search.forAllViewed(server));
                    var index = Math.max(allQuadcopters.lastIndexOf(quadcopter) + spectateDirection, 0);
                    var entity = allQuadcopters.get(index % allQuadcopters.size());
                    serverPlayer.setCamera(entity);
                    QuadzEvents.SWITCH_CAMERA_EVENT.invoker().onSwitchCamera(serverPlayer.getCamera(), entity);
                } else {
                    ItemStack mainHandItem = serverPlayer.getMainHandItem();

                    if (mainHandItem.has(QuadzDataComponentTypes.BINDING)) {
                        @Nullable UUID boundUUID = mainHandItem.getOrDefault(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND).boundUUID();

                        if (boundUUID != null) {
                            Search.forQuadWithBindId(
                                            serverPlayer.level(),
                                            serverPlayer.getCamera().position(),
                                            boundUUID,
                                            server.getPlayerList().getViewDistance() * 16)
                                    .ifPresentOrElse(entity -> {
                                        serverPlayer.setCamera(entity);
                                        QuadzEvents.SWITCH_CAMERA_EVENT.invoker().onSwitchCamera(serverPlayer.getCamera(), entity);
                                    }, () -> Search.forAllViewed(server).stream().findFirst().ifPresent(entity -> {
                                        serverPlayer.setCamera(entity);
                                        QuadzEvents.SWITCH_CAMERA_EVENT.invoker().onSwitchCamera(serverPlayer.getCamera(), entity);
                                    }));
                        }
                    }
                }
            });
        });
    }
}
