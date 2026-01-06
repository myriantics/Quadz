package dev.lazurite.quadz.common.entity.quadcopter;

import dev.lazurite.quadz.common.networking.c2s.ControllerInputC2SPacket;
import net.minecraft.world.phys.Vec3;

public final class QuadcopterController {
    float roll = 0;
    float throttle = 0;

    private int ticksSinceUpdated = -1;
    private static final int MAX_TICKS_WITHOUT_UPDATES = 20;

    public void tick() {
        if (this.ticksSinceUpdated < 0) {
            return;
        } else if (this.ticksSinceUpdated > MAX_TICKS_WITHOUT_UPDATES) {
            this.reset();
        }
    }

    public void update(ControllerInputC2SPacket packet) {
        this.ticksSinceUpdated = 0;

        this.throttle = packet.leftY();
    }

    public void reset() {
        this.ticksSinceUpdated = -1;
        this.roll = 0;
        this.throttle = 0;
    }
}
