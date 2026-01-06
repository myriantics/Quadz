package dev.lazurite.quadz.entity.quadcopter;

import dev.lazurite.quadz.networking.c2s.ControllerInputC2SPacket;
import net.minecraft.world.phys.Vec3;

public final class QuadcopterController {
    private float lateralSpeed = 0;
    private float forwardsSpeed = 0;
    private float verticalSpeed = 0;

    private static final float movementSpeed = 4f/20;

    private int ticksSinceUpdated = -1;
    private static final int MAX_TICKS_WITHOUT_UPDATES = 20;

    public void tick() {
        if (this.ticksSinceUpdated < 0) {
            return;
        } else if (this.ticksSinceUpdated > MAX_TICKS_WITHOUT_UPDATES) {
            this.reset();
        }
    }

    public Vec3 getSpeed() {
        return new Vec3(
                this.forwardsSpeed, this.verticalSpeed, this.lateralSpeed
        ).scale(movementSpeed);
    }

    public void update(ControllerInputC2SPacket packet) {
        this.ticksSinceUpdated = 0;
        this.lateralSpeed = packet.rightX();
        this.forwardsSpeed = packet.rightY();
        this.verticalSpeed = packet.leftY();
    }

    public void reset() {
        this.ticksSinceUpdated = -1;
        this.lateralSpeed = 0;
        this.forwardsSpeed = 0;
        this.verticalSpeed = 0;
    }
}
