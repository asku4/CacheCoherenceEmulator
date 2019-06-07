package io.github.AnaK89.emulator.equipment.utils;

import java.util.ArrayList;
import java.util.List;

public class Logs {
    private final List<String> logs = new ArrayList<>();

    public void add(final String info){
        logs.add(info);
    }

    public List<String> get(){
        return logs;
    }
}