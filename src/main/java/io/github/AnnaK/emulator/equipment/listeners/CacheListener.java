package io.github.AnnaK.emulator.equipment.listeners;

import io.github.AnnaK.emulator.protocol.Message;

public interface CacheListener extends Listener {

    void sendMessage(final Message message);
}
