package dev.lazurite.quadz.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.lazurite.quadz.QuadzClient;
import dev.lazurite.quadz.extension.MinecraftExtension;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void render$TAIL(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        QuadzClient.getQuadcopterFromCamera().ifPresent(quadcopter -> {}
            // quadcopter.getView().onGuiRender(poseStack, tickDelta)
        );
    }

    @WrapMethod(
            method = "renderCrosshair"
    )
    private void quadz$disableCrosshairIfInterfaceActive(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        if (!((MinecraftExtension) Minecraft.getInstance()).quadz$getQuadcopterInterface().isEnabled()) {
            original.call(guiGraphics, deltaTracker);
        }
    }

    @WrapMethod(
            method = "renderExperienceBar"
    )
    public void quadz$disableExperienceBarIfInterfaceActive(GuiGraphics guiGraphics, int i, Operation<Void> original) {
        if (!((MinecraftExtension) Minecraft.getInstance()).quadz$getQuadcopterInterface().isEnabled()) {
            original.call(guiGraphics, i);
        }
    }

}
