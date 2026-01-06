package dev.lazurite.quadz.common.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.UUID;
import java.util.function.Consumer;

public record BindingComponent(UUID boundUUID) implements TooltipProvider {
    public static final MapCodec<BindingComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("boundUUID").forGetter(BindingComponent::boundUUID)
    ).apply(instance, BindingComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BindingComponent> PACKET_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, BindingComponent::boundUUID,
            BindingComponent::new
    );

    public static final BindingComponent UNBOUND = new BindingComponent(null);

    public boolean isBound() {
        return this.boundUUID != null;
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (this.isBound()) {
            if (tooltipFlag.isAdvanced()) {
                consumer.accept(Component.translatable("quadz.tooltip.component.binding.bound_advanced", this.boundUUID()).withColor(CommonColors.LIGHT_GRAY));
            } else {
                consumer.accept(Component.translatable("quadz.tooltip.component.binding.bound").withColor(CommonColors.LIGHT_GRAY));
            }
        } else {

            consumer.accept(Component.translatable("quadz.tooltip.component.binding.unbound").withColor(CommonColors.LIGHT_GRAY));
        }
    }
}
