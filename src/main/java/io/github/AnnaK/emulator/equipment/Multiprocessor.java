package io.github.AnnaK.emulator.equipment;

import java.util.List;

public interface Multiprocessor {
    List<Processor> getProcessors();

    Memory getMemory();

    void stop();
}
