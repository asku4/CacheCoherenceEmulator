package io.github.AnaK89.emulator.protocol.model;

public class Data{
    private final int id;
    private final String message;
    private final String newState;

    //SEND_VALID_INFO
    public Data(final int id, final String message, final String newState){
        this.id = id;
        this.message = message;
        this.newState = newState;
    }

    //WRITE_TO_MEMORY
    public Data(final int id, final String message){
        this.id = id;
        this.message = message;
        this.newState = "";
    }

    //NEED_VALID_INFO, RWITM, BROADCAST_INVALID
    public Data(final int id){
        this.id = id;
        this.message = "";
        this.newState = "";
    }

    //STUB_TO_MEMORY, EMPTY, ERROR_REQUEST
    public Data(){
        this.id = 0;
        this.message = "";
        newState = null;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getNewState() {
        return newState;
    }
}