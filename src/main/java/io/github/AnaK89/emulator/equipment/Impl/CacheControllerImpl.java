package io.github.AnaK89.emulator.equipment.Impl;

import io.github.AnaK89.emulator.equipment.CacheController;
import io.github.AnaK89.emulator.equipment.Processor;
import io.github.AnaK89.emulator.equipment.Listener;
import io.github.AnaK89.emulator.equipment.model.CacheString;
import io.github.AnaK89.emulator.equipment.utils.GeneratorId;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheControllerImpl implements CacheController {
    private static final Logger logger = LogManager.getLogger(CacheControllerImpl.class);
    private final Map<Integer, CacheString> cache = new HashMap<>();
    private final Protocol protocol;
    private final GeneratorId generatorId;
    private final Logs logs;
    private List<Listener> listeners;
    private Processor processor;
    private boolean isRequested = false;

    CacheControllerImpl(
            final Protocol protocol,
            final GeneratorId generatorId,
            final Logs logs) {
        this.protocol = protocol;
        this.logs = logs;
        this.generatorId = generatorId;
    }

    @Override
    public void sendMessage(final Message message) {
        addLog(String.format("%s send message: %s - %d - %s", message.getSender(), message.getType(), message.getData().getId(), message.getData().getMessage()));
        for(final Listener l: listeners){
            if(l.getMessage(message)){
                return;
            }
        }
    }

    @Override
    public boolean getMessage(final Message message) {
        return protocol.cacheProcess(this, message);
    }

    @Override
    public void setListeners(final List<Listener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void writeToOwnCache(String data) {
        writeToOwnCache(generatorId.generate(), data);
    }

    @Override
    public void writeToOwnCache(final int id, final String data) {
        generatorId.updateId(id);
        protocol.writeToOwnCache(this, id, data);
    }

    @Override
    public void requestValidInfo(final int id) {
        protocol.requestValidInfo(this, id);
    }

    @Override
    public void changeStateCacheString(final Integer id, final String state) {
        if ( ! cache.get(id).getState().equals(state)){
            addLog(String.format("%s in %d changed state: %s -> %s", processor.getName(), id, cache.get(id).getState(), state));
            cache.get(id).setState(state);
        }
    }

    @Override
    public String getProcessorName() {
        return processor.getName();
    }

    @Override
    public void isRequest(final boolean req){
        this.isRequested = req;
    }

    @Override
    public boolean isRequested() {
        return isRequested;
    }

    @Override
    public Map<Integer, CacheString> getCache() {
        return cache;
    }

    @Override
    public void addLog(final String log){
        logger.info(log);
        logs.add(log);
    }

    @Override
    public void setProcessor(final Processor processor){
        this.processor = processor;
    }

    @Override
    public int getCountListeners(){
        return listeners.size();
    }
}
