package io.github.AnnaK.emulator.equipment.listeners;

import io.github.AnnaK.emulator.protocol.Message;

public interface Listener {

    void getMessage(final Message message);
}
