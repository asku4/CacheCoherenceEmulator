package io.github.AnaK89.emulator.gui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProcString{
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty state;
    private final SimpleStringProperty value;

    public ProcString(
            final int id,
            final String state,
            final String value){
        this.id = new SimpleIntegerProperty(id);
        this.state = new SimpleStringProperty(state);
        this.value = new SimpleStringProperty(value);
    }

    public void setId(final int id) {
        this.id.set(id);
    }

    public void setState(final String state){
        this.state.set(state);
    }

    public void setValue(final String value) {
        this.value.set(value);
    }

    public int getId() {
        return id.get();
    }

    public String getState(){
        return state.get();
    }

    public String getValue() {
        return value.get();
    }

}