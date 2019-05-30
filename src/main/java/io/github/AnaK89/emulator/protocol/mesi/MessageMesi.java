package io.github.AnaK89.emulator.protocol.mesi;

import io.github.AnaK89.emulator.protocol.Message;
import io.github.AnaK89.emulator.protocol.model.Data;

public class MessageMesi implements Message {
    private final MesiMessageType type;
    private final Data data;
    private final String sender;

    //сообщение для записи/модфикации сущестующих в кэше данных
    MessageMesi(final String sender, final MesiMessageType type, final int id, final String message, final String newState){
        this.sender = sender;
        this.type = type;
        this.data = new Data(id, message, newState);
    }

    //для операции запроса валидной информации и RWITM, BROADCAST_INVALID
    MessageMesi(final String sender, final MesiMessageType type, final int id){
        this.sender = sender;
        this.type = type;
        this.data = new Data(id);
    }

    //для операций STUB_TO_MEMORY, EMPTY
    MessageMesi(final String sender, final MesiMessageType type){
        this.type = type;
        this.data = new Data();
        this.sender = sender;
    }

    //для записи в память
    MessageMesi(final String sender, final MesiMessageType type, final int id, final String message){
        this.sender = sender;
        this.type = type;
        this.data = new Data(id, message);
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
