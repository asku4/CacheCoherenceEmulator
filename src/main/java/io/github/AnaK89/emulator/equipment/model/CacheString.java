package io.github.AnaK89.emulator.equipment.model;

public class CacheString {
    private String state;
    private String data;

    public CacheString(
            final String state,
            final String data){
        this.state = state;
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }
}
