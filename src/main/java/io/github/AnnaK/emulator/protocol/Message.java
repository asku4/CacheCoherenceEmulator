package io.github.AnnaK.emulator.protocol;

import io.github.AnnaK.emulator.protocol.model.Data;
import io.github.AnnaK.emulator.protocol.mesi.MesiMessageType;

public interface Message {

    Data getData();

    MesiMessageType getType();

    String getFrom();

}
