package io.github.AnaK89.emulator.gui.model;

import java.util.ArrayList;
import java.util.List;

public enum Action {
    REQUEST_VALID_INFO("Запрос на валидные данные"),
    WRITE_TO_OWN_CACHE("Запись в КЭШ процессора"),
    WRITE_TO_OWN_CACHE_WITH_ID("Запись в КЭШ процессора по ID"),
    WRITE_TO_MEMORY("Запись в память"),
    WRITE_TO_MEMORY_WITH_ID("Запись в память по ID");

    private final String action;

    Action(final String action){
        this.action = action;
    }

    public static List<String> getAll(){
        List<String> actions = new ArrayList<>();
        for(Action a: values()){
            actions.add(a.action);
        }
        return actions;
    }

    public String toString(){
        return action;
    }

    public static Action getValueOf(final String action){
        for (Action s: values()){
            if(s.action.equals(action)){
                return s;
            }
        }
        return null;
    }
}
