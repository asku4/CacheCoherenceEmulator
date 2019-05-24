package io.github.AnaK89.emulator.equipment.listeners;

import io.github.AnaK89.emulator.protocol.Message;

public interface CacheListener extends Listener {

    void sendMessage(final Message message);
}
