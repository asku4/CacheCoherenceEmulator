package io.github.AnaK89.emulator.equipment.Impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Singleton
public class MemoryImpl implements Memory {
    private static final Logger logger = LogManager.getLogger(MemoryImpl.class);
    private final Protocol protocol;
    private final Map<Integer, String> data;
    private Message prevMessage;

    @Inject
    public MemoryImpl(
            final Protocol protocol,
            final Map<Integer, String> data){
        this.protocol = protocol;
        this.data = data;
    }

    @Override
    public void run() {
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
            logger.info("Write: {} - {}", id, info);
            data.put(id, info);
        }
    }

    public boolean containsData(final Integer id){
        return data.containsKey(id);
    }

    public String getData(final Integer id){
        return data.get(id);
    }

    @Override
    public Map<Integer, String> getAllData(){
        return this.data;
    }
}
