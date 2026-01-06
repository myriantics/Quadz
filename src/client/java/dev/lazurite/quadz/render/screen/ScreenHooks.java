package dev.lazurite.quadz.render.screen;

import dev.lazurite.quadz.QuadzCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class ScreenHooks {

    public static void addQuadzButtonToPauseScreen(PauseScreen screen) {
        screen.addRenderableWidget(getButton(
                screen,
                screen.disconnectButton.getX() + screen.disconnectButton.getWidth() + 5,
                screen.disconnectButton.getY()
        ));
    }

    public static void addQuadzButtonToTitleScreen(TitleScreen screen) {
        screen.addRenderableWidget(getButton(
                screen,
                screen.width / 2 + 128,
                screen.height / 4 + 132)
        );
    }

    private static ImageButton getButton(Screen parent, int x, int y) {
        return new ImageButton(
                20, 20, 0, 0,
                new WidgetSprites(QuadzCommon.locate("textures/gui/quadz.png"), QuadzCommon.locate("textures/gui/quadz.png")),
                button -> Minecraft.getInstance().setScreen(new ControllerSetupScreen(parent)),
                Component.translatable("quadz.config.title")
        );
    }

}
