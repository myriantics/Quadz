package dev.lazurite.quadz.control;

import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.registry.item.QuadzItems;
import dev.lazurite.quadz.tag.QuadzItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class QuadcopterInterface {

    private final ControllerSim controllerSim = new ControllerSim();

    private boolean enabled = false;

    public void tick() {
        if (!validate()) {
            this.disable();
        }

        this.controllerSim.tick(minecraft(), this);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean enable() {
        if (!this.validate()) {
            return false;
        }

        this.enabled = true;
        minecraft().setCameraEntity(minecraft().player.quadz$getActiveQuadcopter());

        return true;
    }

    public void disable() {
        this.enabled = false;
        Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
    }

    private boolean validate() {
        Minecraft minecraft = minecraft();

        // we have to be in a level to have the interface up
        if (minecraft.level == null) {
            return false;
        }

        @Nullable LocalPlayer player = minecraft.player;

        // no player equals no interface
        if (player == null) {
            return false;
        }

        // no active quadcopter equals no interface
        @Nullable Quadcopter activeQuadcopter = player.quadz$getActiveQuadcopter();
        if (activeQuadcopter == null) {
            return false;
        }

        // no goggles equals no interface
        ItemStack helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmetStack.is(QuadzItemTags.QUADCOPTER_INTERFACE_HEADWEAR)) {
            return false;
        }

        // no remote equals no interface
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offhandStack = player.getOffhandItem();
        if (!mainHandStack.is(QuadzItems.REMOTE) && !offhandStack.is(QuadzItems.REMOTE)) {
            return false;
        }

        // if all the checks were passed we win
        return true;
    }

    private static Minecraft minecraft() {
        return Minecraft.getInstance();
    }
}
