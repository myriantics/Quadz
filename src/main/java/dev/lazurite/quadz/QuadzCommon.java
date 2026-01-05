package dev.lazurite.quadz;

import dev.lazurite.quadz.common.registry.*;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import dev.lazurite.quadz.common.registry.item.QuadzItemGroups;
import dev.lazurite.quadz.common.registry.item.QuadzItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuadzCommon implements ModInitializer {

    public static final String MOD_ID = "quadz";
    public static final Logger LOGGER = LogManager.getLogger("Quadz");

    @Override
    public void onInitialize() {
        LOGGER.info("Goggles down, thumbs up!");

        // Registries
        QuadzEntityTypes.init();
        QuadzEvents.init();
        QuadzItems.init();
        QuadzItemGroups.init();
        QuadzPackets.init();
        QuadzDamageTypes.init();
        QuadzDataComponentTypes.init();

        // Events
        // TemplateEvents.ENTITY_TEMPLATE_CHANGED.register(ServerEventHooks::onEntityTemplateChanged);
        // TemplateEvents.TEMPLATE_LOADED.register(ServerEventHooks::onTemplateLoaded);

        // Load templates
        // TemplateLoader.initialize(MOD_ID);

        LOGGER.info("Quadz has loaded!");
    }

    public static ResourceLocation locate(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

}
