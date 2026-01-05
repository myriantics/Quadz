package dev.lazurite.quadz.common.registry.item;

import dev.lazurite.quadz.QuadzCommon;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public abstract class QuadzItemGroups {
    public static final CreativeModeTab QUADZ = register("quadz", builder -> builder
            .title(Component.literal("Quadz"))
            .icon(() -> new ItemStack(QuadzItems.REMOTE_ITEM))
            .displayItems((parameters, output) -> {
                output.accept(QuadzItems.GOGGLES_ITEM);
                output.accept(QuadzItems.QUADCOPTER_ITEM);
                output.accept(QuadzItems.REMOTE_ITEM);
            })
    );

    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Item Groups!");
    }

    private static CreativeModeTab register(String name, Consumer<CreativeModeTab.Builder> consumer) {
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, QuadzCommon.locate(name));
        CreativeModeTab.Builder builder = FabricItemGroup.builder();
        consumer.accept(builder);
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, builder.build());
    }
}
