package dev.lazurite.quadz.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lazurite.quadz.common.registry.item.QuadzItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ControllerConnectedToast implements Toast {

    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement");

    private final Component message;
    private final String controllerName;

    public ControllerConnectedToast(Component message, String controllerName) {
        this.message = message;

        if (controllerName.length() > 25) {
            this.controllerName = controllerName.substring(0, 25) + "...";
        } else {
            this.controllerName = controllerName;
        }
    }

    public static void add(Component message, String name) {
        var manager = Minecraft.getInstance().getToasts();
        manager.addToast(new ControllerConnectedToast(message, name));
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Font font = toastComponent.getMinecraft().font;

        guiGraphics.blit(BACKGROUND_SPRITE, 0, 0, 0, 0, width(), height());
        guiGraphics.drawString(font, message, 30, 7, -1);
        guiGraphics.drawString(font, Component.literal(controllerName), 30, 18, -1);
        guiGraphics.renderFakeItem(new ItemStack(QuadzItems.REMOTE_ITEM), 8, 8);

        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }
}
