package io.github.AnaK89.emulator.gui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RamString{
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty value;

    public RamString(final int id,
                     final String value){
        this.id = new SimpleIntegerProperty(id);
        this.value = new SimpleStringProperty(value);
    }

    public void setId(final int id) {
        this.id.set(id);
    }

    public void setValue(final String value) {
        this.value.set(value);
    }

    public int getId() {
        return id.get();
    }

    public String getValue() {
        return value.get();
    }
}
