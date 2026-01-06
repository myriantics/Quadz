package dev.lazurite.quadz.common.item;

import dev.lazurite.quadz.common.component.BindingComponent;
import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if (livingEntity instanceof Quadcopter quadcopter) {
            if (!player.level().isClientSide()) {
                itemStack.set(QuadzDataComponentTypes.BINDING, new BindingComponent(quadcopter.getUUID()));
                player.displayClientMessage(Component.translatable("quadz.message.entity_bound", quadcopter.getName()), true);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
        }
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

            if (level.isClientSide() && player.isLocalPlayer()) {
                Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
                switch (cameraEntity) {
                    case LocalPlayer localPlayer -> {
                        Quadcopter activeQuadcopter = localPlayer.quadz$getActiveQuadcopter();
                        if (activeQuadcopter != null) {
                            Minecraft.getInstance().setCameraEntity(activeQuadcopter);
                        }
                    }
                    case Quadcopter quadcopter -> {
                        Minecraft.getInstance().setCameraEntity(player);
                    }
                    case null, default -> {}
                }

                return InteractionResultHolder.success(usedStack);
            }
            return super.use(level, player, interactionHand);
        }
    }
}
