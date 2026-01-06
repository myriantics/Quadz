package dev.lazurite.quadz.control;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Joystick {
    private float x = 0;
    private float y = 0;

    public void setX(float x) {
        this.x = Math.clamp(x, -1, 1);
    }

    public void setY(float y) {
        this.y = Math.clamp(y, -1, 1);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
