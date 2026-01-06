package dev.lazurite.quadz.common.registry;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.entity.Quadcopter;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public abstract class QuadzEntityTypes {
    public static final EntityType<Quadcopter> QUADCOPTER = register("quadcopter",
            FabricEntityTypeBuilder.createLiving()
                    .entityFactory(Quadcopter::new)
                    .spawnGroup(MobCategory.MISC)
                    .dimensions(EntityDimensions.scalable(0.5f, 0.2f))
                    .defaultAttributes(LivingEntity::createLivingAttributes)
                    .build()
    );

    static {
        FabricDefaultAttributeRegistry.register(QUADCOPTER, LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 4).build());
    }

    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Entity Types!");
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                QuadzCommon.locate(name),
                type
        );
    }
}
