package dev.lazurite.quadz.common.tag;

import dev.lazurite.quadz.QuadzCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public abstract class QuadzItemTags {



    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, QuadzCommon.locate(name));
    }
}
