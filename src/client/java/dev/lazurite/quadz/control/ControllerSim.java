package dev.lazurite.quadz.control;

import dev.lazurite.quadz.entity.quadcopter.Quadcopter;
import dev.lazurite.quadz.networking.c2s.ControllerInputC2SPacket;
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
            float upVal = options.keyJump.isDown() ? 1 : 0;
            float downVal = options.keyShift.isDown() ? -1 : 0;
            float forwardsVal = options.keyUp.isDown() ? 1 : 0;
            float backwardsVal = options.keyDown.isDown() ? -1 : 0;
            float rightVal = options.keyRight.isDown() ? 1 : 0;
            float leftVal = options.keyLeft.isDown() ? -1 : 0;

            this.left.setY(upVal + downVal);
            this.right.setX(rightVal + leftVal);
            this.right.setY(forwardsVal + backwardsVal);

            this.sync();
        }
    }

    private void sync() {
        ClientPlayNetworking.send(new ControllerInputC2SPacket(this.left, this.right));
    }
}
