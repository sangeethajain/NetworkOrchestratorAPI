package com.swisscom.network.service;
import com.swisscom.network.model.Device;
import com.swisscom.network.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {
    @Mock
    private DeviceRepository repository;
    @InjectMocks
    private DeviceService service;
    // 1. TEST GET ALL
    @Test
    void testGetAllDevices() {
        when(repository.findAll()).thenReturn(List.of(new Device()));
        List<Device> result = service.getAllDevices();
        assertEquals(1, result.size());
        verify(repository).findAll();
    }
    // 2. TEST GET BY ID
    @Test
    void testGetDeviceById() {
        Device device = new Device();
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        Device result = service.getDeviceById(1L);
        assertNotNull(result);
        verify(repository).findById(1L);
    }
    // 3. TEST CREATE
    @Test
    void testCreateDevice() {
        Device device = new Device();
        device.setName("Router-1");
        device.setLocation("datacenter");
        device.setDeviceType("router");
        device.setIpAddress("192.168.1.1");
        device.setStatus("ACTIVE");
        when(repository.save(device)).thenReturn(device);
        Device result = service.createDevice(device);
        assertEquals("Router-1", result.getName());
        verify(repository, times(1)).save(device);
    }
    // 4. TEST DELETE
    @Test
    void testDeleteDevice() {
        doNothing().when(repository).deleteById(1L);
        service.deleteDevice(1L);
        verify(repository, times(1)).deleteById(1L);
    }
    // 5. TEST RESTART LOGIC
    @Test
    void testRestartDevice() {
        Device device = new Device();
        device.setName("Router-1");
        device.setLocation("datacenter");
        device.setStatus("ACTIVE");
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        when(repository.save(any(Device.class))).thenReturn(device);
        Device result = service.restartDevice(1L);
        assertNotNull(result);
        //Check final state
        assertEquals("ACTIVE", result.getStatus());
        //verify DB save happened
        verify(repository, times(1)).save(device);
    }
    // 6. TEST FILTER BY LOCATION
    @Test
    void testGetByLocation() {
        when(repository.findByLocation("office"))
                .thenReturn(List.of(new Device()));
        List<Device> result = service.getDevicesByLocation("office");
        assertEquals(1, result.size());
    }
    // 7. TEST DEVICE NOT FOUND
    @Test
    void testDeviceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getDeviceById(1L));
        assertEquals("Device not found", ex.getMessage());
    }
    // 8. RESTART WHEN DEVICE NOT FOUND
    @Test
    void testRestartDevice_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.restartDevice(1L));

        assertEquals("Device not found", ex.getMessage());
    }
    // 9. DELETE WHEN DEVICE DOES NOT EXIST
    @Test
    void testDeleteDevice_NotFound() {
        doThrow(new RuntimeException("Device not found"))
                .when(repository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.deleteDevice(1L));

        assertEquals("Device not found", ex.getMessage());
    }
    // 10. EMPTY RESULT FOR LOCATION FILTER
    @Test
    void testGetByLocation_Empty() {
        when(repository.findByLocation("office"))
                .thenReturn(List.of());

        List<Device> result = service.getDevicesByLocation("office");

        assertTrue(result.isEmpty());
    }
}