package dev.lazurite.quadz.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lazurite.quadz.client.QuadzClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void render$TAIL(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        QuadzClient.getQuadcopterFromCamera().ifPresent(quadcopter -> {}
            // quadcopter.getView().onGuiRender(poseStack, tickDelta)
        );
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCrosshair$HEAD(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        QuadzClient.getQuadcopterFromCamera().ifPresent(quadcopter -> ci.cancel());
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void renderExperienceBar$HEAD(GuiGraphics guiGraphics, int i, CallbackInfo ci) {
        QuadzClient.getQuadcopterFromCamera().ifPresent(quadcopter -> ci.cancel());
    }

}
