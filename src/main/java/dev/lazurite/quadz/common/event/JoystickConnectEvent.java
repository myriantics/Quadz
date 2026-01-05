package dev.lazurite.quadz.common.event;

@FunctionalInterface
public interface JoystickConnectEvent {
    void onConnect(int id, String name);
}
