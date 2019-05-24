package io.github.AnaK89.emulator.equipment.listeners;

import io.github.AnaK89.emulator.protocol.Message;

public interface Listener {

    void getMessage(final Message message);
}
