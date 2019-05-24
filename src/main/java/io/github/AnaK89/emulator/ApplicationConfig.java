package io.github.AnaK89.emulator;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.github.AnaK89.emulator.equipment.Impl.ProcessorImpl;
import io.github.AnaK89.emulator.protocol.Protocol;
import io.github.AnaK89.emulator.protocol.mesi.MesiProtocolImpl;
import io.github.AnaK89.emulator.equipment.CacheController;
import io.github.AnaK89.emulator.equipment.Impl.CacheControllerImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.AnaK89.emulator.equipment.Processor;

//как-то сделать через контейнер..
@Deprecated
public class ApplicationConfig implements Module {
    private static final Logger logger = LogManager.getLogger(ApplicationConfig.class);

    @Override
    public void configure(Binder binder) {
        binder.bind(CacheController.class).to(CacheControllerImpl.class);
        binder.bind(Processor.class).to(ProcessorImpl.class);
        binder.bind(Protocol.class).to(MesiProtocolImpl.class);
    }
}
