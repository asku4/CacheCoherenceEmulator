package io.github.AnaK89.emulator.equipment;

public interface Processor {
    /**процессор записывает в свой собственный кэш;
     * сама запись происходит не напрямую, а через контроллер,
     * т.к. необходимо также отправлять сообщения на шину о состоянии
     * */
    void writeToOwnCache(final int id, final String data);

    /**совершенно новые данные*/
    void writeToOwnCache(final String data);

    void requestValidInfo(int id);

    CacheController getController();

    String getName();
}
