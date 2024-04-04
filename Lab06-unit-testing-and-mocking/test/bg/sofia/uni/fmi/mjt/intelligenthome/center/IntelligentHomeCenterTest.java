package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {
    @Mock
    private DeviceStorage storage;

    @InjectMocks
    private IntelligentHomeCenter center;

    @Test
    void testRegisterThrowsIllegalArgumentException() throws DeviceAlreadyRegisteredException {
        assertThrows(IllegalArgumentException.class, () -> center.register(null));
    }

    @Test
    void testRegisterThrowsDeviceAlreadyExistsException() throws DeviceAlreadyRegisteredException {
        IoTDevice dev1 = new WiFiThermostat("a", 11, LocalDateTime.now());
        when(storage.exists(any())).thenReturn(true);
        assertThrows(DeviceAlreadyRegisteredException.class, () -> center.register(dev1));
    }

    @Test
    void testRegisterWithValidDevice() throws DeviceAlreadyRegisteredException {
        IoTDevice dev1 = new WiFiThermostat("a", 11, LocalDateTime.now());
        when(storage.exists(any())).thenReturn(false);
        assertDoesNotThrow(() -> center.register(dev1));
    }

    @Test
    void testUnregisterThrowsIllegalArgumentException() throws DeviceNotFoundException {
       assertThrows(IllegalArgumentException.class, () -> center.unregister(null));
    }

    @Test
    void testUnregisterThrowsDeviceAlreadyExistsException() throws DeviceNotFoundException {
        when(storage.exists(any())).thenReturn(false);
        IoTDevice dev1 = new WiFiThermostat("a", 11, LocalDateTime.now());
        assertThrows(DeviceNotFoundException.class, () -> center.unregister(dev1));
    }

    @Test
    void testUnregisterWithValidDevice() throws DeviceNotFoundException {
        when(storage.exists(any())).thenReturn(true);
        IoTDevice dev1 = new WiFiThermostat("a", 11, LocalDateTime.now());
        assertDoesNotThrow(() -> center.unregister(dev1));
    }

    @Test
    void testGetDeviceByIdWithNull() throws DeviceNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> center.getDeviceById(null));
    }

    @Test
    void testGetDeviceByIdThrowsWithEmptyId() throws DeviceNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> center.getDeviceById(""));
    }

    @Test
    void testGetDeviceByIdThrowsWithBlankId() throws DeviceNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> center.getDeviceById("      "));
    }

    @Test
    void testGetDeviceByIdThrowsDeviceNotFoundException() throws DeviceNotFoundException {
        when(storage.exists(any())).thenReturn(false);
        assertThrows(DeviceNotFoundException.class, () -> center.getDeviceById("1"));
    }

    @Test
    void testGetDeviceByIdWithValidDevice() throws DeviceNotFoundException {
        when(storage.exists(any())).thenReturn(true);
        assertDoesNotThrow(() -> center.getDeviceById("2"));
    }

    @Test
    void testGetDeviceQuantityPerTypeThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> center.getDeviceQuantityPerType(null));
    }

    @Test
    void testGetDeviceQuantityPerTypeWithNonExistingType() {
        IoTDevice dev1 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev2 =  new AmazonAlexa("a", 1.11, LocalDateTime.now());
        IoTDevice dev3 =   new WiFiThermostat("b", 1.11, LocalDateTime.now());

        List<IoTDevice> temp4 = Arrays.asList(dev1, dev2, dev3);

        when(storage.listAll()).thenReturn(temp4);
        assertEquals(0, center.getDeviceQuantityPerType(DeviceType.BULB));
    }

    @Test
    void testGetDeviceQuantityPerTypeWithEmptyStorage() {
        List<IoTDevice> temp = Arrays.asList();

        when(storage.listAll()).thenReturn(temp);
        assertEquals(0, center.getDeviceQuantityPerType(DeviceType.BULB));
    }

    @Test
    void testGetDeviceQuantityPerTypeReturnsQuantity() {
        IoTDevice dev1 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev2 = new AmazonAlexa("a", 1.11, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev4 = new RgbBulb("a", 1.11, LocalDateTime.now());
        IoTDevice dev5 = new AmazonAlexa("a", 1.11, LocalDateTime.now());
        IoTDevice dev6 = new AmazonAlexa("a", 1.11, LocalDateTime.now());

            List<IoTDevice> temp1 = Arrays.asList(dev1, dev2, dev3, dev4, dev5, dev6);

        when(storage.listAll()).thenReturn(temp1);
        assertEquals(3, center.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER),
            "getDeviceQuantityPerType => expected 3, but was actual " +
                center.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER));

        assertEquals(2, center.getDeviceQuantityPerType(DeviceType.THERMOSTAT),
            "getDeviceQuantityPerType => expected 2, but was actual " +
                center.getDeviceQuantityPerType(DeviceType.THERMOSTAT));

        assertEquals(1, center.getDeviceQuantityPerType(DeviceType.BULB),
            "getDeviceQuantityPerType => expected 1, but was actual " +
                center.getDeviceQuantityPerType(DeviceType.BULB));
    }

    @Test
    void testGetDeviceQuantityPerTypeAllValuesAreTheSame() {
        IoTDevice dev1 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("a", 1.11, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev4 = new WiFiThermostat("a", 1.11, LocalDateTime.now());

        List<IoTDevice> temp3 = Arrays.asList(dev1, dev2, dev3, dev4);

        when(storage.listAll()).thenReturn(temp3);

        assertEquals(4, center.getDeviceQuantityPerType(DeviceType.THERMOSTAT),
            "getDeviceQuantityPerType => expected 4, but was actual " +
                center.getDeviceQuantityPerType(DeviceType.THERMOSTAT));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> center.getTopNDevicesByPowerConsumption(-10));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionBiggerNThanSize() {

        IoTDevice dev1 = new WiFiThermostat("t", 1.15, LocalDateTime.now());
        IoTDevice dev2 = new WiFiThermostat("a", 2, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("b", 10, LocalDateTime.now());
        IoTDevice dev4 = new WiFiThermostat("c", 20, LocalDateTime.now());

        List<IoTDevice> temp2 = Arrays.asList(dev1, dev2, dev3, dev4);

        when(storage.listAll()).thenReturn(temp2);

        Collection<String> arrList = center.getTopNDevicesByPowerConsumption(Integer.MAX_VALUE);

        assertEquals(4, arrList.size());
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionValidN() {
        IoTDevice dev1 = new WiFiThermostat("t", 1.15, LocalDateTime.now());
        IoTDevice dev2 = new AmazonAlexa("a", 2, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("b", 10, LocalDateTime.now());
        IoTDevice dev4 = new WiFiThermostat("c", 20, LocalDateTime.now());

        List<IoTDevice> temp = Arrays.asList(dev1, dev2, dev3, dev4);

        when(storage.listAll()).thenReturn(temp);

        Collection<String> arrList = center.getTopNDevicesByPowerConsumption(2);

        assertEquals(2, arrList.size());
        assertTrue(arrList.contains(dev1.getId()));
        assertTrue(arrList.contains(dev2.getId()));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWithEmptyStorage() {

        List<IoTDevice> temp5 = new ArrayList<>();

        when(storage.listAll()).thenReturn(temp5);

        Collection<String> arrList = center.getTopNDevicesByPowerConsumption(2);

        assertEquals(0, arrList.size());
    }

    @Test
    void testGetFirstNDevicesByRegistrationThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> center.getFirstNDevicesByRegistration(-10));
    }

    @Test
    void testGetFirstNDevicesByRegistrationBiggerNThanSize() {

        IoTDevice dev1 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev2 = new AmazonAlexa("a", 1.11, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("b", 1.11, LocalDateTime.now());

        dev1.setRegistration(LocalDateTime.now());
        dev2.setRegistration(LocalDateTime.now());
        dev3.setRegistration(LocalDateTime.now());

        List<IoTDevice> temp6 = Arrays.asList(dev1, dev2, dev3);

        when(storage.listAll()).thenReturn(temp6);

        Collection<IoTDevice> arrList = center.getFirstNDevicesByRegistration(Integer.MAX_VALUE);

        assertEquals(3, arrList.size());
    }

    @Test
    void testGetFirstNDevicesByRegistrationWithValidN() {

        IoTDevice dev1 = new WiFiThermostat("b", 1.11, LocalDateTime.now());
        IoTDevice dev2 = new AmazonAlexa("a", 1.11, LocalDateTime.now());
        IoTDevice dev3 = new WiFiThermostat("b", 1.11, LocalDateTime.now());

        dev1.setRegistration(LocalDateTime.now());
        dev2.setRegistration(LocalDateTime.now());
        dev3.setRegistration(LocalDateTime.now());

        List<IoTDevice> temp7 = Arrays.asList(dev1, dev2, dev3);

        when(storage.listAll()).thenReturn(temp7);

        Collection<IoTDevice> arrList = center.getFirstNDevicesByRegistration(2);

        assertEquals(2, arrList.size());
        assertTrue(arrList.contains(dev1));
        assertTrue(arrList.contains(dev2));
    }

    @Test
    void testGetFirstNDevicesByRegistrationWithEmptyStorage() {

        List<IoTDevice> temp8 = new ArrayList<>();

        when(storage.listAll()).thenReturn(temp8);

        Collection<IoTDevice> arrList = center.getFirstNDevicesByRegistration(2);

        assertEquals(0, arrList.size());
    }
}
