package io.github.AnnaK.emulator.equipment.utils;

import com.google.inject.Singleton;

@Singleton
public class GeneratorId {
    private static int lastId = 0;

    public int generate(){
        return ++lastId;
    }

    public void updateId(final int i){
        lastId = i;
    }
}
