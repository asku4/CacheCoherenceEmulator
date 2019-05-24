package io.github.AnnaK.emulator;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.github.AnnaK.emulator.equipment.Impl.ProcessorImpl;
import io.github.AnnaK.emulator.protocol.Protocol;
import io.github.AnnaK.emulator.protocol.mesi.MesiProtocolImpl;
import io.github.AnnaK.emulator.equipment.CacheController;
import io.github.AnnaK.emulator.equipment.Impl.CacheControllerImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.AnnaK.emulator.equipment.Processor;

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
