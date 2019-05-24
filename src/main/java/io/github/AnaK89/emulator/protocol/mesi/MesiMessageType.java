package io.github.AnaK89.emulator.protocol.mesi;

public enum MesiMessageType {
    BROADCAST_INVALID,
    NEED_VALID_INFO,
    SEND_VALID_INFO,
    READ_WITH_INTENT_TO_MODIFY,     // чтение с целью модификации
    WRITE_TO_MEMORY,
    STUB_TO_MEMORY,
    EMPTY                           // пустое сообщение для правильной работы памяти
}
