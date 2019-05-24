package io.github.AnnaK.emulator.equipment.Impl;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.AnnaK.emulator.equipment.CacheController;
import io.github.AnnaK.emulator.equipment.Processor;
import io.github.AnnaK.emulator.equipment.utils.GeneratorId;

public class ProcessorImpl implements Processor {
    private static final Logger logger = LogManager.getLogger(ProcessorImpl.class);
    private final String name;

    private final CacheController controller;
    private static final GeneratorId GENERATOR_ID = new GeneratorId();

    @Inject
    public ProcessorImpl(
            final String name,
            final CacheController controller){
        logger.info("Processor: {} - Controller: {}", name, controller);
        this.name = name;
        this.controller = controller;
    }

    //процессор записывает в свой собственный кэш; сама запись происходит не напрямую, а через контроллер, т.к. необходимо также отправлять сообщения на шину о состоянии
    @Override
    public void writeToOwnCache(final int id, final String data) {
        controller.writeToOwnCache(id, data);
    }

    //совершенно новые данные
    public void writeToOwnCache(final String data){
        writeToOwnCache(GENERATOR_ID.generate(), data);
    }

    @Override
    public void requestValidInfo(final int id){
        controller.requestValidInfo(id);
    }

    @Override
    public CacheController getController() {
        return controller;
    }

    @Override
    public String getName() {
        return name;
    }
}
