package io.github.AnnaK.emulator.equipment.Impl;

import com.google.inject.Inject;
import io.github.AnnaK.emulator.equipment.CacheController;
import io.github.AnnaK.emulator.equipment.model.CacheString;
import io.github.AnnaK.emulator.equipment.listeners.Listener;
import io.github.AnnaK.emulator.protocol.Message;
import io.github.AnnaK.emulator.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheControllerImpl implements CacheController {
    private static final Logger logger = LogManager.getLogger(CacheControllerImpl.class);
    private final Map<Integer, CacheString> cache;
    private final Protocol protocol;
    private final String processorName;
    private List<Listener> listeners;
    private boolean isRequested = false;

    @Inject
    public CacheControllerImpl(
            final String processorName,
            final HashMap<Integer, CacheString> cache,
            final Protocol protocol) {
        this.processorName = processorName;
        this.cache = cache;
        this.protocol = protocol;
    }

    @Override
    public void run() {
    }

    @Override
    public void sendMessage(final Message message) {
        logger.info("{} send message: {} - {} - {}", message.getFrom(), message.getType(), message.getData().getId(), message.getData().getMessage());
        for(final Listener l: listeners){
            l.getMessage(message);
        }
    }

    @Override
    public void getMessage(final Message message) {
        protocol.cacheProcess(this, message);
    }

    @Override
    public void setListeners(final List<Listener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void writeToOwnCache(final int id, final String data) {
        protocol.writeToOwnCache(this, id, data);
    }

    @Override
    public void requestValidInfo(final int id) {
        protocol.requestValidInfo(this, id);
    }

    @Override
    public void newMessage(final Message message) {
        sendMessage(message);
    }

    @Override
    public boolean containsCacheString(final Integer id) {
        return cache.containsKey(id);
    }

    @Override
    public CacheString getCacheString(final Integer id) {
        return cache.get(id);
    }

    @Override
    public void addCacheString(final Integer id, final CacheString cacheString) {
        cache.put(id, cacheString);
    }

    @Override
    public void changeStateCacheString(final Integer id, final String state) {
        if ( ! cache.get(id).getState().equals(state)){
            logger.info("{} in {} changed state: {} -> {}", processorName, id, cache.get(id).getState(), state);
            cache.get(id).setState(state);
        }
    }

    @Override
    public String getProcessorName() {
        return processorName;
    }

    @Override
    public void isRequest(final boolean req){
        this.isRequested = req;
    }

    @Override
    public boolean isRequested() {
        return isRequested;
    }
}
