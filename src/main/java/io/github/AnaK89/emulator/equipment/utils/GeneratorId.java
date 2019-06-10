package io.github.AnaK89.emulator.equipment.utils;

import java.util.HashSet;
import java.util.Set;

public class GeneratorId {
    private final Set<Integer> insertsId = new HashSet<>();
    private int lastId = 0;

    public int generate(){
        while (insertsId.contains(lastId)){
            lastId++;
        }
        return lastId++;
    }

    public void updateId(final int i){
        insertsId.add(i);
    }
}
