package io.github.AnaK89.emulator.equipment.utils;

import com.google.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class GeneratorId {
    private final Set<Integer> insertsId = new HashSet<>();
    private static int lastId = 0;

    public int generate(){
        lastId++;
        while (insertsId.contains(lastId)){
            lastId++;
        }
        return lastId;
    }

    public void updateId(final int i){
        insertsId.add(i);
    }
}
