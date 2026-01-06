package dev.lazurite.quadz.item;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.component.BindingComponent;
import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.registry.item.QuadzDataComponentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
        BindingComponent original = usedStack.getOrDefault(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND);

        int fart = 0;
        if (player.isCrouching()) {
            @Nullable Quadcopter quadcopter = player.quadz$getActiveQuadcopter();
            if (quadcopter != null && quadcopter.getUUID().equals(original.boundUUID())) {
                player.quadz$clearActiveQuadcopter();
            }

            if (!level.isClientSide()) {
                usedStack.set(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND);
                player.displayClientMessage(Component.translatable("quadz.message.binding_cleared"), true);
            }
        }

        return InteractionResultHolder.success(usedStack);
    }

}
