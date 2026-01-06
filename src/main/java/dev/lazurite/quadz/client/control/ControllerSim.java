package dev.lazurite.quadz.client.control;

import dev.lazurite.quadz.common.control.Joystick;
import dev.lazurite.quadz.common.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.common.networking.c2s.ControllerInputC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class ControllerSim {
    private final Joystick left = new Joystick();
    private final Joystick right = new Joystick();

    private final Minecraft minecraft;

    public ControllerSim(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void tick() {
        Options options = this.minecraft.options;
        if (this.minecraft.cameraEntity instanceof Quadcopter) {
            float forwardsVal = options.keyUp.isDown() ? 1 : 0;
            float backwardsVal = options.keyDown.isDown() ? -1 : 0;
            float rightVal = options.keyRight.isDown() ? 1 : 0;
            float leftVal = options.keyLeft.isDown() ? -1 : 0;

            this.left.setY(forwardsVal + backwardsVal);
            this.right.setX(rightVal + leftVal);

            this.sync();
        }
    }

    private void sync() {
        ClientPlayNetworking.send(new ControllerInputC2SPacket(this.left, this.right));
    }
}
