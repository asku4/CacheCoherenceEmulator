package io.github.AnaK89.emulator.equipment.Impl;

import com.google.inject.Inject;
import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.Listener;
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
    //private final List<Thread> threads= new ArrayList<>();

    @Inject
    public MultiprocessorImpl(final int quantityProcessors) {
        List<Listener> listeners = new ArrayList<>();

        for(int i = 1; i <= quantityProcessors; i++){
            final String name = "Processor" + i;
            final CacheController cacheController = new CacheControllerImpl(new MesiProtocolImpl(logs), logs);
            listeners.add(cacheController);
            final Processor processor = new ProcessorImpl(name, cacheController);
            cacheController.setProcessor(processor);
            processors.add(processor);
        }
        this.memory = new MemoryImpl(new MesiProtocolImpl(logs), logs);
        listeners.add(memory);

        for(final Processor p: processors){
            p.getController().setListeners(listeners);
        }
        memory.setListeners(listeners);

        /*final Thread memoryThread = new Thread(memory);
        memoryThread.start();
        threads.add(memoryThread);
        for (final Processor processor: processors){
            final Thread cacheThread = new Thread(processor.getController());
            cacheThread.start();
            threads.add(cacheThread);
        }*/
    }

    /*@Override
    public void stop() {
        for(final Thread thread: threads){
            thread.stop();
        }
    }*/

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

    @Override
    public void addLog(final String log) {
        logs.add(log);
    }
}
