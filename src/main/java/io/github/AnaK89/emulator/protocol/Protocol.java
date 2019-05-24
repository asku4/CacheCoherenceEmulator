package io.github.AnaK89.emulator.protocol;

import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.CacheController;

public interface Protocol {
    void cacheProcess(final CacheController cacheController, final Message message);

    void memoryProcess(final Memory memory, final Message message, final Message nextMessage);

    void writeToOwnCache(final CacheController cacheController, final int id, final String data);

    void requestValidInfo(final CacheController cacheController, final int id);
}
