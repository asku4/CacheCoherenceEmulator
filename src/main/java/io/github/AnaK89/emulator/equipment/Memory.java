package io.github.AnaK89.emulator.equipment;

import java.util.Map;

public interface Memory extends Listener {
    String NAME = "Memory";

    void write(final int id, final String info);

    void writeFromOutside(String info);

    void writeFromOutside(int id, String info);

    Map<Integer, String> getData();

    void addLog(final String log);
}
