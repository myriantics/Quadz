package dev.lazurite.quadz.mixin.self;

import com.llamalad7.mixinextras.sugar.Local;
import dev.lazurite.quadz.component.BindingComponent;
import dev.lazurite.quadz.control.QuadcopterInterface;
import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.extension.MinecraftExtension;
import dev.lazurite.quadz.item.RemoteItem;
import dev.lazurite.quadz.registry.item.QuadzDataComponentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RemoteItem.class)
public abstract class RemoteItemMixin extends Item {
    public RemoteItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "use",
            at = @At(value = "RETURN")
    )
    private void quadz$swapCameras(
            Level level,
            Player player,
            InteractionHand interactionHand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        if (player.getItemInHand(interactionHand).getOrDefault(QuadzDataComponentTypes.BINDING, BindingComponent.UNBOUND).isBound() && player instanceof LocalPlayer) {
            Minecraft minecraft = Minecraft.getInstance();
            QuadcopterInterface quadcopterInterface = ((MinecraftExtension) minecraft).quadz$getQuadcopterInterface();

            if (quadcopterInterface.isEnabled()) {
                quadcopterInterface.disable();
                player.displayClientMessage(Component.translatable("quadz.message.interface_disabled"), true);
            } else {
                quadcopterInterface.enable();
                player.displayClientMessage(Component.translatable("quadz.message.interface_enabled"), true);
            }
        }
    }
}
