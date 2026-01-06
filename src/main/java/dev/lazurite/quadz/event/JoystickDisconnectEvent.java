package dev.lazurite.quadz.event;

@FunctionalInterface
public interface JoystickDisconnectEvent {
    void onDisconnect(int id, String name);
}
