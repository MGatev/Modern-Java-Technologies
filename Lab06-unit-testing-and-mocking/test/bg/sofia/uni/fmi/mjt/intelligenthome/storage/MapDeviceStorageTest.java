package bg.sofia.uni.fmi.mjt.intelligenthome.storage;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapDeviceStorageTest {

    @Test
    void testDeleteWithNull(){
        MapDeviceStorage m = new MapDeviceStorage();
        assertFalse(m.delete(null));
    }

    @Test
    void testDeleteWithValid(){
        MapDeviceStorage m = new MapDeviceStorage();
        IoTDevice dev = new WiFiThermostat("a", 1, LocalDateTime.now());
        m.store("1", dev);
        assertTrue(m.delete("1"));
    }

    @Test
    void testGetWithValidId(){
        MapDeviceStorage m = new MapDeviceStorage();
        IoTDevice dev = new WiFiThermostat("a", 1, LocalDateTime.now());
        m.store("1", dev);
        assertEquals(dev, m.get("1"));
    }

    @Test
    void testExistsWithValidId(){
        MapDeviceStorage m = new MapDeviceStorage();
        IoTDevice dev = new WiFiThermostat("a", 1, LocalDateTime.now());
        m.store("1", dev);
        assertTrue(m.exists("1"));
    }

    @Test
    void testListAllWithValidValues(){
        MapDeviceStorage m = new MapDeviceStorage();
        IoTDevice dev = new WiFiThermostat("a", 1, LocalDateTime.now());
        IoTDevice dev2 = new AmazonAlexa("b", 1, LocalDateTime.now());

        m.store("1", dev);
        m.store("2", dev2);

        Collection<IoTDevice> c = new ArrayList<>();
        c.add(dev);
        c.add(dev2);

        assertIterableEquals(c, m.listAll());
    }
}
