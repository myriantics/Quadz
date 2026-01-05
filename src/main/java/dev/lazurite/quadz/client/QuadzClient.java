package dev.lazurite.quadz.client;

import dev.lazurite.quadz.client.render.QuadcopterView;
import dev.lazurite.quadz.client.render.entity.QuadcopterEntityRenderer;
import dev.lazurite.quadz.common.registry.QuadzEntityTypes;
import dev.lazurite.quadz.common.registry.QuadzEvents;
import dev.lazurite.quadz.common.util.Bindable;
import dev.lazurite.quadz.common.util.Search;
import dev.lazurite.quadz.client.event.ClientEventHooks;
import dev.lazurite.quadz.client.resource.SplashResourceLoader;
import dev.lazurite.quadz.common.entity.Quadcopter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;

import java.util.Optional;

public class QuadzClient implements ClientModInitializer {

    /**
     * Finds the player's quadcopter based on its camera view.
     */
    public static Optional<Quadcopter> getQuadcopterFromCamera() {
        return Optional.empty();

        /*
        return ViewStack.getInstance()
                .peek()
                .filter(view -> view instanceof QuadcopterView)
                .map(view -> ((QuadcopterView) view).getQuadcopter());
         */
    }

    /**
     * Finds the player's quadcopter based on its held remote.
     * <p>
     * Only works if it's within a 256 block radius.
     */
    public static Optional<Quadcopter> getQuadcopterFromRemote() {
        var player = Minecraft.getInstance().player;

        if (player != null) {
            return Bindable.get(player.getMainHandItem()).flatMap(remote -> Search.forQuadWithBindId(player.level, player.position(), remote.getBindId(), 256));
        }

        return Optional.empty();
    }

    @Override
    public void onInitializeClient() {
        Config.load();

        // Renderer
        EntityRendererRegistry.register(QuadzEntityTypes.QUADCOPTER, QuadcopterEntityRenderer::new);
        // FormRegistry.register((Templated.Item) QuadzCommon.QUADCOPTER_ITEM);

        // Splash screen text injection
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SplashResourceLoader());

        // Events
        ClientTickEvents.START_WORLD_TICK.register(ClientEventHooks::onClientLevelTick);
        ClientPlayConnectionEvents.JOIN.register(ClientEventHooks::onPostLogin);
        ClientTickEvents.START_CLIENT_TICK.register(ClientEventHooks::onClientTick);
        QuadzEvents.JOYSTICK_CONNECT.register(ClientEventHooks::onJoystickConnect);
        QuadzEvents.JOYSTICK_DISCONNECT.register(ClientEventHooks::onJoystickDisconnect);
        QuadzEvents.LEFT_CLICK_EVENT.register(ClientEventHooks::onLeftClick);
        QuadzEvents.RIGHT_CLICK_EVENT.register(ClientEventHooks::onRightClick);
    }

}
