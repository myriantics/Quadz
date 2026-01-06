package dev.lazurite.quadz.event;

@FunctionalInterface
public interface JoystickConnectEvent {
    void onConnect(int id, String name);
}
