package io.github.AnaK89.emulator.equipment;

import io.github.AnaK89.emulator.protocol.Message;

public interface Listener {

    void sendMessage(final Message message);

    void getMessage(final Message message);
}
