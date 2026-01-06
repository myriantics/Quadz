package dev.lazurite.quadz.tag;

import dev.lazurite.quadz.QuadzCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public abstract class QuadzItemTags {

    public static final TagKey<Item> QUADCOPTER_INTERFACE_HEADWEAR = create("quadcopter_interface_headwear");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, QuadzCommon.locate(name));
    }
}
