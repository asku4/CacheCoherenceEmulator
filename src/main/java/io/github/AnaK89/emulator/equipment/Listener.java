package io.github.AnaK89.emulator.equipment;

import io.github.AnaK89.emulator.protocol.Message;

import java.util.List;

public interface Listener {

    void sendMessage(final Message message);

    boolean getMessage(final Message message);

    void setListeners(final List<Listener> listeners);
}
