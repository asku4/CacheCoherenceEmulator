package io.github.AnaK89.emulator.equipment;

import io.github.AnaK89.emulator.equipment.model.CacheString;

import java.util.Map;

public interface CacheController extends Listener {

    void writeToOwnCache(final String data);

    void writeToOwnCache(final int id, final String data);

    void requestValidInfo(final int id);

    void changeStateCacheString(final Integer id, final String state);

    void isRequest(final boolean req);

    boolean isRequested();

    Map<Integer, CacheString> getCache();

    void setProcessor(final Processor processor);

    String getProcessorName();

    void addLog(final String log);

    int getCountListeners();
}
