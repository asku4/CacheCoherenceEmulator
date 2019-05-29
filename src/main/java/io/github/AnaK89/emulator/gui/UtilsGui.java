package io.github.AnaK89.emulator.gui;

import io.github.AnaK89.emulator.equipment.model.CacheString;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import io.github.AnaK89.emulator.gui.model.ProcString;
import io.github.AnaK89.emulator.gui.model.RamString;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

class UtilsGui {

    ObservableList<ProcString> toProcStringList(final Map<Integer, CacheString> cache){
        final List<ProcString> result = new ArrayList<>();
        for (Integer id: cache.keySet()){
            result.add(new ProcString(id, cache.get(id).getState(), cache.get(id).getData()));
        }
        return FXCollections.observableArrayList(result);
    }

    ObservableList<RamString> toRamStringList(final Map<Integer, String> ram){
        final List<RamString> result = new ArrayList<>();
        for (Integer id: ram.keySet()){
            result.add(new RamString(id, ram.get(id)));
        }
        return FXCollections.observableArrayList(result);
    }

    ObservableList<String> toSystemMessage(final Logs logs){
        return FXCollections.observableArrayList(logs.get());
    }
}

