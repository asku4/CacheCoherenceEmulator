package io.github.AnaK89.emulator.protocol.mesi;

import io.github.AnaK89.emulator.equipment.*;
import io.github.AnaK89.emulator.equipment.Impl.MultiprocessorImpl;
import io.github.AnaK89.emulator.equipment.model.CacheString;
import io.github.AnaK89.emulator.equipment.utils.Logs;
import org.junit.Test;

import static io.github.AnaK89.emulator.protocol.mesi.MesiMessageType.*;

import static org.junit.Assert.*;

public class MesiProtocolImplTest {
    private final MesiProtocolImpl protocol = new MesiProtocolImpl(new Logs());

    @Test
    public void cacheProcess() {
        final String sender = "sender";
        final String validState = "Valid";
        final Multiprocessor multiprocessor = new MultiprocessorImpl(2);
        final CacheController cacheController0 = multiprocessor.getProcessors().get(0).getController();

        // READ_WITH_INTENT_TO_MODIFY
        {
            //anyState
            cacheController0.getCache().put(1, new CacheString(validState, "ex1"));
            protocol.cacheProcess(cacheController0, new MessageMesi(sender, READ_WITH_INTENT_TO_MODIFY, 1));
            assertEquals(StateMesi.I.toString(), cacheController0.getCache().get(1).getState());

            //Modified
            cacheController0.getCache().put(2, new CacheString(StateMesi.M.toString(), "ex2"));
            protocol.cacheProcess(cacheController0, new MessageMesi(sender, READ_WITH_INTENT_TO_MODIFY, 2));
            assertEquals(StateMesi.I.toString(), cacheController0.getCache().get(2).getState());
        }

        // BROADCAST_INVALID
        {
            cacheController0.getCache().put(3, new CacheString(validState, "ex3"));
            protocol.cacheProcess(cacheController0, new MessageMesi(sender, BROADCAST_INVALID, 3));
            assertEquals(StateMesi.I.toString(), cacheController0.getCache().get(3).getState());
        }

        // NEED_VALID_INFO
        final String info = "fromMemory";
        {
            // Modified
            cacheController0.changeStateCacheString(2, StateMesi.M.toString());
            protocol.cacheProcess(cacheController0, new MessageMesi(sender, NEED_VALID_INFO, 2));
            assertTrue(multiprocessor.getMemory().getData().containsKey(2));
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(2).getState());
            assertEquals("ex2", cacheController0.getCache().get(2).getData());

            // Another
            cacheController0.changeStateCacheString(3, StateMesi.E.toString());
            protocol.cacheProcess(cacheController0, new MessageMesi(sender, NEED_VALID_INFO, 3));
            assertFalse(multiprocessor.getMemory().getData().containsKey(3));
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(3).getState());
            assertEquals("ex3", cacheController0.getCache().get(3).getData());

            // Invalid

            multiprocessor.getMemory().write(4, info);
            multiprocessor.getProcessors().get(0).getController().requestValidInfo(4);
            assertEquals(StateMesi.E.toString(), cacheController0.getCache().get(4).getState());
            assertEquals(info, cacheController0.getCache().get(4).getData());
        }

        //SEND_VALID_INFO
        {
            multiprocessor.getProcessors().get(1).getController().requestValidInfo(4);
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(4).getState());
            assertEquals(StateMesi.S.toString(), multiprocessor.getProcessors().get(1).getController().getCache().get(4).getState());
            assertEquals(info, multiprocessor.getProcessors().get(1).getController().getCache().get(4).getData());
        }
    }

    @Test
    public void memoryProcess() {
        final Multiprocessor multiprocessor = new MultiprocessorImpl(2);
        final Memory memory = multiprocessor.getMemory();
        final String data = "toMemory";

        // first scenario: WRITE_TO_MEMORY and EMPTY(not stub_to_memory)
        {
            protocol.memoryProcess(memory, new MessageMesi("Proc1", WRITE_TO_MEMORY, 1, data), new MessageMesi("Proc1", EMPTY));
            assertTrue(memory.getData().containsKey(1));
            assertEquals(data, memory.getData().get(1));
        }

        // second scenario: WRITE_TO_MEMORY and STUB_TO_MEMORY
        {
            protocol.memoryProcess(memory, new MessageMesi("Proc1", WRITE_TO_MEMORY, 2, data), new MessageMesi("Proc2", STUB_TO_MEMORY));
            assertFalse(memory.getData().containsKey(2));
        }

        // third scenario: NEED_VALID_INFO and EMPTY(not stub_to_memory)
        {
            final CacheController cacheController0 = multiprocessor.getProcessors().get(0).getController();
            cacheController0.requestValidInfo(1);
            assertTrue(cacheController0.getCache().containsKey(1));
            assertEquals(StateMesi.E.toString(), cacheController0.getCache().get(1).getState());
            assertEquals(data, cacheController0.getCache().get(1).getData());
        }

        // last: NEED_VALID_INFO and STUB_TO_MEMORY
        {
            final String newData = "newData";
            final CacheController cacheController1 = multiprocessor.getProcessors().get(1).getController();
            multiprocessor.getProcessors().get(0).getController().writeToOwnCache(1, newData);
            cacheController1.requestValidInfo(1);
            assertTrue(memory.getData().containsKey(1));
            assertEquals(newData, memory.getData().get(1));
            assertTrue(cacheController1.getCache().containsKey(1));
            assertEquals(StateMesi.S.toString(), cacheController1.getCache().get(1).getState());
            assertEquals(newData, cacheController1.getCache().get(1).getData());
        }
    }

    @Test
    public void writeToOwnCache() {
        final Multiprocessor multiprocessor = new MultiprocessorImpl(2);
        final CacheController cacheController0 = multiprocessor.getProcessors().get(0).getController();
        final int id = 0;

        // don't contains this id
        {
            final String data = "newData";
            cacheController0.writeToOwnCache(data);
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(data, cacheController0.getCache().get(id).getData());
            assertEquals(StateMesi.M.toString(), cacheController0.getCache().get(id).getState());
        }

        // contains in cache with M state
        {
            final String data = "newData1";
            cacheController0.writeToOwnCache(id, data);
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(data, cacheController0.getCache().get(id).getData());
            assertEquals(StateMesi.M.toString(), cacheController0.getCache().get(id).getState());
        }

        // contains in cache with not M/E state (S)
        {
            final CacheController cacheController1 = multiprocessor.getProcessors().get(1).getController();
            final String data = "newData2";
            cacheController1.requestValidInfo(id);
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(id).getState());
            cacheController0.writeToOwnCache(id, data);
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(data, cacheController0.getCache().get(id).getData());
            assertEquals(StateMesi.M.toString(), cacheController0.getCache().get(id).getState());
            assertTrue(cacheController1.getCache().containsKey(id));
            assertNotSame(data, cacheController1.getCache().get(id).getData());
            assertEquals(StateMesi.I.toString(), cacheController1.getCache().get(id).getState());
        }
    }

    @Test
    public void requestValidInfo() {
        final Multiprocessor multiprocessor = new MultiprocessorImpl(2);
        final CacheController cacheController0 = multiprocessor.getProcessors().get(0).getController();
        final CacheController cacheController1 = multiprocessor.getProcessors().get(1).getController();
        final String data = "data";
        final int id = 0;

        // don't contains this id
        {
            cacheController1.writeToOwnCache(data);
            assertTrue(cacheController1.getCache().containsKey(id));
            assertEquals(StateMesi.M.toString(), cacheController1.getCache().get(id).getState());
            cacheController0.requestValidInfo(id);
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(data, cacheController0.getCache().get(id).getData());
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(id).getState());
            assertEquals(StateMesi.S.toString(), cacheController1.getCache().get(id).getState());
            assertTrue(multiprocessor.getMemory().getData().containsKey(id));
            assertEquals(data, multiprocessor.getMemory().getData().get(id));
        }

        // contains this id and state - not I
        {
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(data, cacheController0.getCache().get(id).getData());
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(id).getState());
            cacheController0.requestValidInfo(id);
            assertTrue(cacheController0.getCache().containsKey(id));
            assertEquals(data, cacheController0.getCache().get(id).getData());
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(id).getState());
            assertEquals(StateMesi.S.toString(), cacheController1.getCache().get(id).getState());
        }

        // contains this id and state - I
        {
            final String newData = "newData";
            cacheController0.writeToOwnCache(id, newData);
            assertEquals(StateMesi.M.toString(), cacheController0.getCache().get(id).getState());
            assertEquals(StateMesi.I.toString(), cacheController1.getCache().get(id).getState());
            assertEquals(data, multiprocessor.getMemory().getData().get(id));
            cacheController1.requestValidInfo(id);
            assertEquals(newData, cacheController1.getCache().get(id).getData());
            assertEquals(StateMesi.S.toString(), cacheController1.getCache().get(id).getState());
            assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(id).getState());
            assertEquals(newData, multiprocessor.getMemory().getData().get(id));
        }
    }

    @Test
    public void broadcastInvalid() {
        final Multiprocessor multiprocessor = new MultiprocessorImpl(2);
        final CacheController cacheController0 = multiprocessor.getProcessors().get(0).getController();
        final CacheController cacheController1 = multiprocessor.getProcessors().get(1).getController();
        final int id = 0;

        cacheController0.writeToOwnCache("123");
        cacheController1.requestValidInfo(id);
        assertTrue(cacheController0.getCache().containsKey(id));
        assertEquals(StateMesi.S.toString(), cacheController0.getCache().get(id).getState());
        assertTrue(cacheController1.getCache().containsKey(id));
        assertEquals(StateMesi.S.toString(), cacheController1.getCache().get(id).getState());
        multiprocessor.getMemory().writeFromOutside(id, "new");
        assertEquals(StateMesi.I.toString(), cacheController0.getCache().get(id).getState());
        assertEquals(StateMesi.I.toString(), cacheController1.getCache().get(id).getState());
    }
}