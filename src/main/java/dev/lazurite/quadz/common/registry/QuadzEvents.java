package dev.lazurite.quadz.common.registry;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.event.Click;
import dev.lazurite.quadz.common.event.JoystickConnectEvent;
import dev.lazurite.quadz.common.event.JoystickDisconnectEvent;
import dev.lazurite.quadz.common.event.SwitchCameraEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.Consumer;

public abstract class QuadzEvents {

    public static final Event<SwitchCameraEvent> SWITCH_CAMERA_EVENT = EventFactory.createArrayBacked(
            SwitchCameraEvent.class,
            callbacks -> (previousCameraEntity, currentCameraEntity) -> {
                for (SwitchCameraEvent callback : callbacks) {
                    callback.onSwitchCamera(previousCameraEntity, currentCameraEntity);
                }
            });

    public static Event<Click> RIGHT_CLICK_EVENT = EventFactory.createArrayBacked(
            Click.class,
            callbacks -> () -> {
                for (Click callback : callbacks) {
                    callback.onClick();
                }
            });

    public static Event<Click> LEFT_CLICK_EVENT = EventFactory.createArrayBacked(
            Click.class,
            callbacks -> () -> {
                for (Click callback : callbacks) {
                    callback.onClick();
                }
            });;

    public static final Event<JoystickConnectEvent> JOYSTICK_CONNECT = EventFactory.createArrayBacked(
            JoystickConnectEvent.class,
            callbacks -> (id, name) -> {
                for (JoystickConnectEvent callback : callbacks) {
                    callback.onConnect(id, name);
                }
            });

    public static final Event<JoystickDisconnectEvent> JOYSTICK_DISCONNECT = EventFactory.createArrayBacked(
            JoystickDisconnectEvent.class,
            callbacks -> (id, name) -> {
                for (JoystickDisconnectEvent callback : callbacks) {
                    callback.onDisconnect(id, name);
                }
            });


    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Events!");
    }
}
