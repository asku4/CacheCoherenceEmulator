package io.github.AnaK89.emulator.equipment.utils;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class Logs {
    private final List<String> logs = new ArrayList<>();

    public void add(final String info){
        logs.add(info);
    }

    public List<String> get(){
        return logs;
    }
}