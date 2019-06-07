package io.github.AnaK89.emulator.protocol.mesi;

import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.protocol.model.Data;

public class MessageMesi implements Message {
    private final MesiMessageType type;
    private final Data data;
    private final String sender;

    //SEND_VALID_INFO
    MessageMesi(final String sender, final MesiMessageType type, final int id, final String data, final String newState){
        this.sender = sender;
        this.type = type;
        this.data = new Data(id, data, newState);
    }

    //NEED_VALID_INFO, RWITM, BROADCAST_INVALID
    MessageMesi(final String sender, final MesiMessageType type, final int id){
        this.sender = sender;
        this.type = type;
        this.data = new Data(id);
    }

    //STUB_TO_MEMORY, EMPTY, ERROR_REQUEST
    MessageMesi(final String sender, final MesiMessageType type){
        this.type = type;
        this.data = new Data();
        this.sender = sender;
    }

    //WRITE_TO_MEMORY
    MessageMesi(final String sender, final MesiMessageType type, final int id, final String data){
        this.sender = sender;
        this.type = type;
        this.data = new Data(id, data);
    }

    public Data getData() {
        return data;
    }

    public MesiMessageType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }
}
