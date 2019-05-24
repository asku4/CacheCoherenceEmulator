package io.github.AnnaK.emulator.protocol.mesi;

import io.github.AnnaK.emulator.equipment.Memory;
import io.github.AnnaK.emulator.protocol.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.AnnaK.emulator.equipment.CacheController;
import io.github.AnnaK.emulator.equipment.model.CacheString;
import io.github.AnnaK.emulator.protocol.Protocol;

import java.util.Objects;

import static io.github.AnnaK.emulator.protocol.mesi.MesiMessageType.*;

public class MesiProtocolImpl implements Protocol {
    private static final Logger logger = LogManager.getLogger(MesiProtocolImpl.class);

    @Override
    public void cacheProcess(final CacheController cacheController, final Message message) {
        switch (message.getType()){
            case READ_WITH_INTENT_TO_MODIFY:
                responseToRWITM(cacheController, message);
                break;
            case BROADCAST_INVALID:
                responseBroadcastInvalid(cacheController, message);
                break;
            case NEED_VALID_INFO:       //todo: если никто не ответил, взять инфу из памяти, хотя у нас такого не может быть в принципе (но это неточно)
                responseValidInfo(cacheController, message);
                break;
            case SEND_VALID_INFO:
                acceptValidInfo(cacheController, message);
                break;
        }
    }

    @Override
    public void memoryProcess(final Memory memory, final Message message, final Message nextMessage){
        if(message.getType().equals(WRITE_TO_MEMORY) && ! nextMessage.getType().equals(STUB_TO_MEMORY)){
            memory.write(message.getData().getId(), message.getData().getMessage());
        }
    }

    @Override
    public void writeToOwnCache(final CacheController cacheController, final int id, final String data){
        if (cacheController.containsCacheString(id)){
            if(! StateMesi.M.toString().equals(cacheController.getCacheString(id).getState())){
                cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), BROADCAST_INVALID, id));
                logger.info("Broadcast invalid");
            }
        } else {
            cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), READ_WITH_INTENT_TO_MODIFY, id));
        }
        final CacheString cacheString = new CacheString(StateMesi.M.toString(), data);
        cacheController.addCacheString(id, cacheString);
        logger.info("{} add or change cacheString: {} - {} - {}", cacheController.getProcessorName(), id, cacheString.getData(), cacheString.getState());
    }

    @Override
    public void requestValidInfo(final CacheController cacheController, final int id){
        cacheController.isRequest(true);
        cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), NEED_VALID_INFO, id));
    }

    private void responseValidInfo(final CacheController cacheController, final Message message) {
        final int id = message.getData().getId();
        if( ! cacheController.containsCacheString(id) || message.getFrom().equals(cacheController.getProcessorName()) ){
            return;
        }

        switch (Objects.requireNonNull(StateMesi.getValueOf(cacheController.getCacheString(id).getState()))){
            case M:
                cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), WRITE_TO_MEMORY, id, cacheController.getCacheString(id).getData()));
            case E:
            case S:
                cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), SEND_VALID_INFO, id, cacheController.getCacheString(id).getData(), StateMesi.S.toString())); //todo: исправить проблему множественного ответа(а надо ли?)
                cacheController.changeStateCacheString(id, StateMesi.S.toString());
                break;
        }
    }

    private void responseToRWITM(final CacheController cacheController, final Message message){
        if(message.getFrom() != null && ! message.getFrom().equals(cacheController.getProcessorName())){
            final int id = message.getData().getId();
            if (cacheController.containsCacheString(id)){
                if(StateMesi.M.toString().equals(cacheController.getCacheString(id).getState())){
                    cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), STUB_TO_MEMORY));
                    cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), WRITE_TO_MEMORY, id, cacheController.getCacheString(id).getData()));
                    cacheController.newMessage(new MessageMesi(cacheController.getProcessorName(), EMPTY));
                }
                cacheController.changeStateCacheString(id, StateMesi.I.toString());
            }
        }

    }

    private void responseBroadcastInvalid(final CacheController cacheController, final Message message){
        if(message.getFrom() != null && ! message.getFrom().equals(cacheController.getProcessorName()) && cacheController.containsCacheString(message.getData().getId())){
            cacheController.changeStateCacheString(message.getData().getId(), StateMesi.I.toString());
        }
    }

    private void acceptValidInfo(final CacheController cacheController, final Message message){
        if(cacheController.isRequested()
                && message.getFrom() != null
                && ! message.getFrom().equals(cacheController.getProcessorName())){
            final CacheString cacheString = new CacheString(message.getData().getNewState(), message.getData().getMessage());
            logger.info("{} accept: {} - {}", cacheController.getProcessorName(), cacheString.getState(), cacheString.getData());
            cacheController.addCacheString(message.getData().getId(), cacheString);
            cacheController.isRequest(false);
        }
    }
}
