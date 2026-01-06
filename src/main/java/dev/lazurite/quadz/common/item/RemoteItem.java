package dev.lazurite.quadz.common.item;

import dev.lazurite.quadz.common.component.BindingComponent;
import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Represents a held "Transmitter" or remote for controlling a quadcopter.
 * However, this class is not likely to contain much of the logic.
 */
public class RemoteItem extends Item {

    public RemoteItem() {
        super(new Properties().stacksTo(1).component(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack usedStack = player.getItemInHand(interactionHand);

        if (player.isCrouching()) {
            if (!level.isClientSide()) {
                usedStack.set(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND);
                player.displayClientMessage(Component.translatable("quadz.message.binding_cleared"), true);
            }
            return InteractionResultHolder.success(usedStack);
        } else {

            if (player instanceof ServerPlayer serverPlayer) {
                Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
                switch (cameraEntity) {
                    case LocalPlayer localPlayer -> {
                        Quadcopter activeQuadcopter = localPlayer.quadz$getActiveQuadcopter();
                        if (activeQuadcopter != null) {
                            serverPlayer.setCamera(activeQuadcopter);
                        }
                    }
                    case Quadcopter quadcopter -> {
                        serverPlayer.setCamera(serverPlayer);
                    }
                    case null, default -> {}
                }

                return InteractionResultHolder.success(usedStack);
            }
            return super.use(level, player, interactionHand);
        }
    }
}
