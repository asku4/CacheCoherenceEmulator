package io.github.AnaK89.emulator.equipment;

import io.github.AnaK89.emulator.equipment.listeners.Listener;

import java.util.Map;

public interface Memory extends Runnable, Listener {

    void write(final int id, final String info);

    Map<Integer, String> getAllData();

}
