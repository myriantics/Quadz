package dev.lazurite.quadz;

import dev.lazurite.form.api.event.TemplateEvents;
import dev.lazurite.form.api.loader.TemplateLoader;
import dev.lazurite.quadz.common.hooks.ServerNetworkEventHooks;
import dev.lazurite.quadz.common.registry.QuadzEntityTypes;
import dev.lazurite.quadz.common.registry.QuadzItemGroups;
import dev.lazurite.quadz.common.registry.QuadzItems;
import dev.lazurite.toolbox.api.network.PacketRegistry;
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

        PacketRegistry.registerServerbound(Networking.REQUEST_QUADCOPTER_VIEW, ServerNetworkEventHooks::onQuadcopterViewRequested);

        // Load templates
        TemplateLoader.initialize(MOD_ID);

        LOGGER.info("Quadz has loaded!");
    }

    public static class Networking {

    }

    public static ResourceLocation locate(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

}
