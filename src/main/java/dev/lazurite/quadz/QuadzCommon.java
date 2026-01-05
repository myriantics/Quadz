package dev.lazurite.quadz;

import dev.lazurite.quadz.common.registry.QuadzEntityTypes;
import dev.lazurite.quadz.common.registry.QuadzItemGroups;
import dev.lazurite.quadz.common.registry.QuadzItems;
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
        QuadzItems.init();
        QuadzItemGroups.init();

        // Events
        // TemplateEvents.ENTITY_TEMPLATE_CHANGED.register(ServerEventHooks::onEntityTemplateChanged);
        // TemplateEvents.TEMPLATE_LOADED.register(ServerEventHooks::onTemplateLoaded);

        // Load templates
        // TemplateLoader.initialize(MOD_ID);

        LOGGER.info("Quadz has loaded!");
    }

    public static class Networking {

    }

    public static ResourceLocation locate(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

}
