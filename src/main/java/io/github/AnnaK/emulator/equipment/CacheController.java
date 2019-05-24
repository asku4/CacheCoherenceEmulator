package io.github.AnnaK.emulator.equipment;

import io.github.AnnaK.emulator.equipment.listeners.CacheListener;
import io.github.AnnaK.emulator.equipment.listeners.Listener;
import io.github.AnnaK.emulator.protocol.Message;
import io.github.AnnaK.emulator.equipment.model.CacheString;

import java.util.List;

public interface CacheController extends Runnable, CacheListener {

    void setListeners(final List<Listener> listeners);

    void writeToOwnCache(final int id, final String data);

    void requestValidInfo(final int id);

    void newMessage(final Message message);

    boolean containsCacheString(final Integer id);

    CacheString getCacheString(final Integer id);

    void addCacheString(final Integer id, final CacheString cacheString);

    void changeStateCacheString(final Integer id, final String state);

    String getProcessorName();

    void isRequest(boolean req);

    boolean isRequested();

}
