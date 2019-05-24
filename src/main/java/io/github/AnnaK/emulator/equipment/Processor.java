package io.github.AnnaK.emulator.equipment;

public interface Processor {
    void writeToOwnCache(final int id, final String data);
    void writeToOwnCache(final String data);

    void requestValidInfo(int id);

    CacheController getController();

    String getName();
}
