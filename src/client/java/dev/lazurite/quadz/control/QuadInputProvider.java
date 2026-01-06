package dev.lazurite.quadz.control;

import net.minecraft.client.Options;

public abstract class QuadInputProvider {
    protected final Options options;

    protected QuadInputProvider(Options options) {
        this.options = options;
    }

    public void tick() {

    }

    public void sync() {

    }
}
