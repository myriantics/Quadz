package dev.lazurite.quadz.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
    private void renderCrosshair$HEAD(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        if (QuadzClient.getQuadcopterFromCamera().isEmpty()) {
            original.call(guiGraphics, deltaTracker);
        }
    }

    @WrapMethod(
            method = "renderExperienceBar"
    )
    public void renderExperienceBar$HEAD(GuiGraphics guiGraphics, int i, Operation<Void> original) {
        if (QuadzClient.getQuadcopterFromCamera().isEmpty()) {
            original.call(guiGraphics, i);
        }
    }

}
