package io.github.AnaK89.emulator.equipment;

import java.util.List;
import java.util.Map;

public interface Memory extends Listener {

    void write(final int id, final String info);

    boolean containsData(Integer id);

    String getData(Integer id);

    Map<Integer, String> getAllData();

    void setListeners(List<Listener> listeners);

    void addLog(final String log);
}
