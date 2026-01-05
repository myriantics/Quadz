package dev.lazurite.quadz.common.event;

@FunctionalInterface
public interface JoystickDisconnectEvent {
    void onDisconnect(int id, String name);
}
