package io.github.AnaK89.emulator.protocol.mesi;

import io.github.AnaK89.emulator.equipment.Listener;
import io.github.AnaK89.emulator.equipment.Memory;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import io.github.AnaK89.emulator.protocol.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.AnaK89.emulator.equipment.CacheController;
import io.github.AnaK89.emulator.equipment.model.CacheString;
import io.github.AnaK89.emulator.protocol.Protocol;

import java.util.Objects;

import static io.github.AnaK89.emulator.protocol.mesi.MesiMessageType.*;

public class MesiProtocolImpl implements Protocol {
    private static final Logger logger = LogManager.getLogger(MesiProtocolImpl.class);
    private final Logs logs;

    public MesiProtocolImpl(final Logs logs){
        this.logs = logs;
    }

    @Override
    public boolean cacheProcess(final CacheController cacheController, final Message message) {
        switch (message.getType()){
            case READ_WITH_INTENT_TO_MODIFY:
                responseToRWITM(cacheController, message);
                break;
            case BROADCAST_INVALID:
                responseBroadcastInvalid(cacheController, message);
                break;
            case NEED_VALID_INFO:
                return responseValidInfo(cacheController, message);
            case SEND_VALID_INFO:
                return acceptValidInfo(cacheController, message);
            case ERROR_REQUEST:
                cacheController.isRequest(false);
        }
        return false;
    }

    @Override
    public void memoryProcess(final Memory memory, final Message message, final Message nextMessage){
        if(message.getType().equals(WRITE_TO_MEMORY) && ! nextMessage.getType().equals(STUB_TO_MEMORY)){
            memory.write(message.getData().getId(), message.getData().getMessage());
        }

        if(message.getType().equals(NEED_VALID_INFO) && ! nextMessage.getType().equals(STUB_TO_MEMORY)){
            final int id = message.getData().getId();
            if(memory.getData().containsKey(id)){
                memory.sendMessage(new MessageMesi(Memory.class.getName(), SEND_VALID_INFO, id, memory.getData().get(id), StateMesi.E.toString()));
            } else {
                addLog(ERROR_REQUEST.toString());
                memory.sendMessage(new MessageMesi(Memory.class.getName(), ERROR_REQUEST));
            }
        }
    }

    @Override
    public void writeToOwnCache(final CacheController cacheController, final int id, final String data){
        if (cacheController.getCache().containsKey(id)){
            final String state = cacheController.getCache().get(id).getState();
            if(! StateMesi.M.toString().equals(state) && ! StateMesi.E.toString().equals(state)){
                broadcastInvalid(cacheController, cacheController.getProcessorName(), id);
                addLog("Broadcast invalid");
            }
        } else {
            cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), READ_WITH_INTENT_TO_MODIFY, id));
        }
        final CacheString cacheString = new CacheString(StateMesi.M.toString(), data);
        cacheController.getCache().put(id, cacheString);
        addLog(String.format("%s add or change cacheString: %d - %s - %s", cacheController.getProcessorName(), id, cacheString.getData(), cacheString.getState()));
    }

    @Override
    public void requestValidInfo(final CacheController cacheController, final int id){
        cacheController.isRequest(true);
        cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), NEED_VALID_INFO, id));
    }

    @Override
    public void broadcastInvalid(final Listener listener, final String name, final int id){
        listener.sendMessage(new MessageMesi(name, BROADCAST_INVALID, id));
    }

    private static int countNullProc = 0;
    private boolean responseValidInfo(final CacheController cacheController, final Message message) {
        final int id = message.getData().getId();
        if( ! cacheController.getCache().containsKey(id) || cacheController.getCache().get(id).getState().equals(StateMesi.I.toString())){
            countNullProc++;
            if(countNullProc == cacheController.getCountListeners() - 1){
                countNullProc = 0;
                cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), EMPTY));
                return true;
            }
            return false;
        }

        countNullProc = 0;
        cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), STUB_TO_MEMORY));
        switch (Objects.requireNonNull(StateMesi.getValueOf(cacheController.getCache().get(id).getState()))){
            case M:
                cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), WRITE_TO_MEMORY, id, cacheController.getCache().get(id).getData()));
            case E:
            case S:
                cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), SEND_VALID_INFO, id, cacheController.getCache().get(id).getData(), StateMesi.S.toString()));
                cacheController.changeStateCacheString(id, StateMesi.S.toString());
                break;
        }
        return true;
    }

    private void responseToRWITM(final CacheController cacheController, final Message message){
        final int id = message.getData().getId();
        if (cacheController.getCache().containsKey(id)){
            if(StateMesi.M.toString().equals(cacheController.getCache().get(id).getState())){
                cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), STUB_TO_MEMORY));
                cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), WRITE_TO_MEMORY, id, cacheController.getCache().get(id).getData()));
                cacheController.sendMessage(new MessageMesi(cacheController.getProcessorName(), EMPTY));
            }
            cacheController.changeStateCacheString(id, StateMesi.I.toString());
        }
    }

    private void responseBroadcastInvalid(final CacheController cacheController, final Message message){
        if(message.getSender() != null && ! message.getSender().equals(cacheController.getProcessorName()) && cacheController.getCache().containsKey(message.getData().getId())){
            cacheController.changeStateCacheString(message.getData().getId(), StateMesi.I.toString());
        }
    }

    private boolean acceptValidInfo(final CacheController cacheController, final Message message){
        if(cacheController.isRequested()){
            final CacheString cacheString = new CacheString(message.getData().getNewState(), message.getData().getMessage());
            addLog(String.format("%s accept: %s - %s", cacheController.getProcessorName(), cacheString.getState(), cacheString.getData()));
            cacheController.getCache().put(message.getData().getId(), cacheString);
            cacheController.isRequest(false);
            return true;
        }
        return false;
    }

    private void addLog(final String log){
        logger.info(log);
        logs.add(log);
    }
}
