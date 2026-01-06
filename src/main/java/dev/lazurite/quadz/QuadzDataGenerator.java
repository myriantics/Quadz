package dev.lazurite.quadz;

import dev.lazurite.quadz.common.datagen.QuadzDamageTypeProvider;
import dev.lazurite.quadz.common.datagen.QuadzItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class QuadzDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(QuadzItemTagProvider::new);
        pack.addProvider(QuadzDamageTypeProvider::new);
    }
}
