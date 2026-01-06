package dev.lazurite.quadz.common.registry.item;

import com.mojang.serialization.Codec;
import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.component.BindingComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;

import java.util.function.UnaryOperator;

public abstract class QuadzDataComponentTypes {

    public static final DataComponentType<BindingComponent> BINDING = register(
            "bound_id",
            integerBuilder -> integerBuilder
                    .persistent(BindingComponent.CODEC.codec())
                    .networkSynchronized(BindingComponent.PACKET_CODEC)
    );

    public static void init() {
        QuadzCommon.LOGGER.info("Initialized Quadz' Data Component Types!");
    }

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, QuadzCommon.locate(name), builderOperator.apply(DataComponentType.builder()).build());
    }

    private static DataComponentType<Unit> registerUnit(String name) {
        return register(name, unitBuilder -> unitBuilder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
    }
}
