package io.github.AnaK89.emulator.equipment.Impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.AnaK89.emulator.equipment.Listener;
import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Singleton
public class MemoryImpl implements Memory {
    private static final Logger logger = LogManager.getLogger(MemoryImpl.class);
    private final Protocol protocol;
    private final Logs logs;
    private final Map<Integer, String> data = new HashMap<>();
    private List<Listener> listeners;
    private Message prevMessage;

    @Inject
    public MemoryImpl(
            final Protocol protocol,
            final Logs logs){
        this.protocol = protocol;
        this.logs = logs;
    }

    /*@Override
    public void run() {
    }*/

    @Override
    public void sendMessage(Message message) {
        for(final Listener l: listeners){
            l.getMessage(message);
        }
    }

    @Override
    public void getMessage(final Message message) {
        if(prevMessage == null){
            prevMessage = message;
            return;
        }

        protocol.memoryProcess(this, prevMessage, message);
        prevMessage = message;
    }

    @Override
    public void write(final int id, final String info){
        if( ! data.containsKey(id) || (data.containsKey(id) && ! data.get(id).equals(info))){
            addLog(String.format("Memory write: %d - %s", id, info));
            data.put(id, info);
        }
    }

    @Override
    public boolean containsData(final Integer id){
        return data.containsKey(id);
    }

    @Override
    public String getData(final Integer id){
        return data.get(id);
    }

    @Override
    public Map<Integer, String> getAllData(){
        return this.data;
    }

    @Override
    public void setListeners(final List<Listener> listeners){
        this.listeners = listeners;
    }

    @Override
    public void addLog(final String log){
        logger.info(log);
        logs.add(log);
    }
}
