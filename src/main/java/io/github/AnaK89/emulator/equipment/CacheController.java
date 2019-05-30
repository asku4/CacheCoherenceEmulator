package io.github.AnaK89.emulator.equipment;

import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.equipment.model.CacheString;

import java.util.List;
import java.util.Map;

public interface CacheController extends Listener {

    void setListeners(final List<Listener> listeners);

    void writeToOwnCache(final String data);

    void writeToOwnCache(final int id, final String data);

    void requestValidInfo(final int id);

    void sendMessage(final Message message);

    boolean containsCacheString(final Integer id);

    CacheString getCacheString(final Integer id);

    void addCacheString(final Integer id, final CacheString cacheString);

    void changeStateCacheString(final Integer id, final String state);

    String getProcessorName();

    void isRequest(final boolean req);

    boolean isRequested();

    Map<Integer, CacheString> getCache();

    void addLog(final String log);

    void setProcessor(final Processor processor);
}
