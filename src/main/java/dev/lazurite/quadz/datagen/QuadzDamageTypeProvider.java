package dev.lazurite.quadz.datagen;

import dev.lazurite.quadz.registry.QuadzDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class QuadzDamageTypeProvider extends FabricDynamicRegistryProvider {
    public QuadzDamageTypeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        entries.add(QuadzDamageTypes.DIVEBOMBING, new DamageType(
                "divebombing",
                DamageScaling.NEVER,
                0.5f
        ));
        entries.add(QuadzDamageTypes.QUADCOPTER, new DamageType(
                "quadcopter",
                DamageScaling.NEVER,
                0.2f
        ));
    }

    @Override
    public String getName() {
        return "quadz_damage_type_provider";
    }
}
