package dev.lazurite.quadz.common.registry.item;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.item.GogglesItem;
import dev.lazurite.quadz.common.item.QuadcopterItem;
import dev.lazurite.quadz.common.item.RemoteItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public abstract class QuadzItems {

    public static final Item GOGGLES_ITEM = register("goggles", new GogglesItem());
    public static final Item QUADCOPTER_ITEM = register("quadcopter", new QuadcopterItem());
    public static final Item REMOTE_ITEM = register("remote", new RemoteItem());

    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Items!");
    }

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, QuadzCommon.locate(name), item);
    }
}
