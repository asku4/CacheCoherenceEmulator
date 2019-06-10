package io.github.AnaK89.emulator.equipment.Impl;

import io.github.AnaK89.emulator.equipment.Listener;
import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.utils.GeneratorId;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MemoryImpl implements Memory {
    private static final Logger logger = LogManager.getLogger(MemoryImpl.class);
    private final Protocol protocol;
    private final GeneratorId generatorId;
    private final Logs logs;
    private final Map<Integer, String> data = new HashMap<>();
    private List<Listener> listeners;
    private Message prevMessage;

    MemoryImpl(
            final Protocol protocol,
            final GeneratorId generatorId,
            final Logs logs){
        this.protocol = protocol;
        this.logs = logs;
        this.generatorId = generatorId;
    }

    @Override
    public void sendMessage(Message message) {
        for(final Listener l: listeners){
            l.getMessage(message);
        }
    }

    @Override
    public boolean getMessage(final Message message) {
        if(prevMessage == null){
            prevMessage = message;
            return false;
        }

        protocol.memoryProcess(this, prevMessage, message);
        prevMessage = message;
        return false;
    }

    @Override
    public void write(final int id, final String info){
        if( ! data.containsKey(id) || (data.containsKey(id) && ! data.get(id).equals(info))){
            generatorId.updateId(id);
            addLog(String.format("Memory write: %d - %s", id, info));
            data.put(id, info);
        }
    }

    @Override
    public void writeFromOutside(final String info){
        write(generatorId.generate(), info);
    }

    @Override
    public void writeFromOutside(final int id, final String info){
        protocol.broadcastInvalid(this, NAME, id);
        write(id, info);
    }

    @Override
    public Map<Integer, String> getData(){
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
