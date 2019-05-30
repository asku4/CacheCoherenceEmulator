package io.github.AnaK89.emulator.protocol;

import io.github.AnaK89.emulator.protocol.model.Data;
import io.github.AnaK89.emulator.protocol.mesi.MesiMessageType;

public interface Message {

    Data getData();

    MesiMessageType getType();

    String getSender();

}
