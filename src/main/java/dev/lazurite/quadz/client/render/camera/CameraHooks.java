package dev.lazurite.quadz.client.render.camera;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.client.QuadzClient;
import dev.lazurite.quadz.client.networking.QuadzClientPlayNetworking;
import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.networking.c2s.RequestPlayerViewC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestQuadcopterViewC2SPacket;
import dev.lazurite.quadz.common.registry.QuadzPackets;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class CameraHooks {

    private static int index = 0;

    public static void onCameraReset() {
        index = 0;
        Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
        QuadzClientPlayNetworking.send(RequestPlayerViewC2SPacket.INSTANCE);
    }

    public static Optional<CameraType> onCycle() {
        LocalPlayer player = Minecraft.getInstance().player;

        /*
            Check to make sure the conditions for player viewing haven't changed.
            If they have changed, switch the player's view back to themself.
         */
        Consumer<Quadcopter> changeIndex = quadcopter -> {
            var continueViewing = quadcopter.shouldPlayerBeViewing(player);
            index = (index + 1) % (continueViewing ? 5 : 3);

            if (index >= 0 && index <= 2) {
                if (QuadzClient.getQuadcopterFromCamera().isPresent()) {
                    QuadzClientPlayNetworking.send(RequestPlayerViewC2SPacket.INSTANCE);
                }
            }
        };

        @Nullable Quadcopter quadcopter = player.quadz$getActiveQuadcopter();
        if (quadcopter != null) {
            changeIndex.accept(quadcopter);
        } else {
            index = (index + 1) % 3;
        }

        return switch (index) {
            case 0 -> Optional.of(CameraType.FIRST_PERSON);
            case 1 -> Optional.of(CameraType.THIRD_PERSON_BACK);
            case 2 -> Optional.of(CameraType.THIRD_PERSON_FRONT);
            case 3 -> {
                QuadzClientPlayNetworking.send(new RequestQuadcopterViewC2SPacket(0));
                yield Optional.of(CameraType.FIRST_PERSON);
            }
            case 4 -> {
                if (QuadzClient.getQuadcopterFromCamera().isEmpty()) {
                    index = 1;
                    yield Optional.of(CameraType.THIRD_PERSON_BACK);
                }
                yield Optional.empty();
            }
            default -> Optional.empty();
        };
    }

}
