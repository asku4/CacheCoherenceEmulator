package io.github.AnaK89.emulator.equipment.Impl;

import com.google.inject.Inject;
import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.listeners.Listener;
import io.github.AnaK89.emulator.protocol.mesi.MesiProtocolImpl;
import io.github.AnaK89.emulator.equipment.CacheController;
import io.github.AnaK89.emulator.equipment.Multiprocessor;
import io.github.AnaK89.emulator.equipment.Processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiprocessorImpl implements Multiprocessor {
    private final Memory memory;
    private final List<Processor> processors = new ArrayList<>();
    private final List<Thread> threads= new ArrayList<>();

    @Inject
    public MultiprocessorImpl(final int quantityProcessors) {
        List<Listener> listeners = new ArrayList<>();

        this.memory = new MemoryImpl(new MesiProtocolImpl(), new HashMap<>());
        listeners.add(memory);

        for(int i = 1; i <= quantityProcessors; i++){
            final String name = "Processor" + i;
            final CacheController cacheController = new CacheControllerImpl(name, new HashMap<>(), new MesiProtocolImpl());
            listeners.add(cacheController);
            final Processor processor = new ProcessorImpl(name, cacheController);
            processors.add(processor);
        }

        for(final Processor p: processors){
            p.getController().setListeners(listeners);
        }

        /*final Thread memoryThread = new Thread(memory);
        memoryThread.start();
        threads.add(memoryThread);
        for (final Processor processor: processors){
            final Thread cacheThread = new Thread(processor.getController());
            cacheThread.start();
            threads.add(cacheThread);
        }*/
    }

    @Override
    public void stop() {
        for(final Thread thread: threads){
            thread.stop();
        }
    }

    @Override
    public Memory getMemory() {
        return memory;
    }

    @Override
    public List<Processor> getProcessors() {
        return processors;
    }
}
