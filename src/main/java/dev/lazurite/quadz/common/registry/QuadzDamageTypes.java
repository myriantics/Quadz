package dev.lazurite.quadz.common.registry;

import dev.lazurite.quadz.QuadzCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class QuadzDamageTypes {

    public static final ResourceKey<DamageType> DIVEBOMBING = create("divebombing");
    public static final ResourceKey<DamageType> QUADCOPTER = create("quadcopter");

    public static void init() {
        QuadzCommon.LOGGER.info("Initialized Quadz' Damage Types!");
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, QuadzCommon.locate(name));
    }
}
