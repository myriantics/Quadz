package dev.lazurite.quadz.common.registry;

import dev.lazurite.quadz.QuadzCommon;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public abstract class QuadzItemGroups {
    public static final CreativeModeTab QUADZ = register("quadz", builder -> builder
            .title(Component.literal("Quadz"))
            .icon(() -> new ItemStack(QuadzItems.REMOTE_ITEM))
            .displayItems((featureFlagSet, output, bl) -> {
                output.accept(QuadzItems.GOGGLES_ITEM);
                output.accept(QuadzItems.QUADCOPTER_ITEM);
                output.accept(QuadzItems.REMOTE_ITEM);
            })
    );

    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Item Groups!");
    }

    private static CreativeModeTab register(String name, Consumer<CreativeModeTab.Builder> consumer) {
        CreativeModeTab.Builder builder = FabricItemGroup.builder(QuadzCommon.locate(name));
        consumer.accept(builder);
        CreativeModeTab itemGroup = builder.build();
        ItemGroupEvents.modifyEntriesEvent(itemGroup);
        return itemGroup;
    }
}
