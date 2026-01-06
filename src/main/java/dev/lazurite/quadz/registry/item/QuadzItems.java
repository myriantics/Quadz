package dev.lazurite.quadz.registry.item;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.item.GogglesItem;
import dev.lazurite.quadz.item.QuadcopterItem;
import dev.lazurite.quadz.item.RemoteItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public abstract class QuadzItems {

    public static final Item GOGGLES = register("goggles", new GogglesItem());
    public static final Item QUADCOPTER = register("quadcopter", new QuadcopterItem());
    public static final Item REMOTE = register("remote", new RemoteItem());

    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Items!");
    }

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, QuadzCommon.locate(name), item);
    }
}
