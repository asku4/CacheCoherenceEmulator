package io.github.AnaK89.emulator.equipment.Impl;

import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.Listener;
import io.github.AnaK89.emulator.equipment.utils.GeneratorId;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import io.github.AnaK89.emulator.protocol.mesi.MesiProtocolImpl;
import io.github.AnaK89.emulator.equipment.CacheController;
import io.github.AnaK89.emulator.equipment.Multiprocessor;
import io.github.AnaK89.emulator.equipment.Processor;

import java.util.ArrayList;
import java.util.List;

public class MultiprocessorImpl implements Multiprocessor {
    private final Memory memory;
    private final List<Processor> processors = new ArrayList<>();
    private static final Logs logs = new Logs();

    public MultiprocessorImpl(final int quantityProcessors) {
        final List<Listener> listeners = new ArrayList<>();
        final GeneratorId generatorId = new GeneratorId();

        this.memory = new MemoryImpl(new MesiProtocolImpl(logs), generatorId, logs);
        listeners.add(memory);
        for(int i = 1; i <= quantityProcessors; i++){
            final String name = "Processor" + i;
            final CacheController cacheController = new CacheControllerImpl(new MesiProtocolImpl(logs), generatorId, logs);
            listeners.add(cacheController);
            final Processor processor = new ProcessorImpl(name, cacheController);
            cacheController.setProcessor(processor);
            processors.add(processor);
        }

        for(final Processor p: processors){
            setListeners(p.getController(), listeners);
        }
        setListeners(memory, listeners);
    }

    @Override
    public Memory getMemory() {
        return memory;
    }

    @Override
    public List<Processor> getProcessors() {
        return processors;
    }

    @Override
    public Logs getLogs() {
        return logs;
    }

    @SuppressWarnings("unchecked")
    private void setListeners(final Listener listener, final List<Listener> listeners){
        final List<Listener> resultListeners = (List<Listener>) ((ArrayList<Listener>) listeners).clone();
        resultListeners.remove(listener);
        listener.setListeners(resultListeners);
    }
}
