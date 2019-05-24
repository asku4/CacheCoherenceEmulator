package io.github.AnnaK.emulator.protocol.mesi;

import io.github.AnnaK.emulator.protocol.Message;
import io.github.AnnaK.emulator.protocol.model.Data;

public class MessageMesi implements Message {
    private final MesiMessageType type;
    private final Data data;
    private final String from;

    //сообщение для записи/модфикации сущестующих в кэше данных
    MessageMesi(final String from, final MesiMessageType type, final int id, final String message, final String newState){
        this.from = from;
        this.type = type;
        this.data = new Data(id, message, newState);
    }

    //для операции запроса валидной информации и RWITM, BROADCAST_INVALID
    MessageMesi(final String from, final MesiMessageType type, final int id){
        this.from = from;
        this.type = type;
        this.data = new Data(id);
    }

    //для операций STUB_TO_MEMORY, EMPTY
    MessageMesi(final String from, final MesiMessageType type){
        this.type = type;
        this.data = new Data();
        this.from = from;
    }

    //для записи в память
    MessageMesi(final String from, final MesiMessageType type, final int id, final String message){
        this.from = from;
        this.type = type;
        this.data = new Data(id, message);
    }

    public Data getData() {
        return data;
    }

    public MesiMessageType getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }
}
