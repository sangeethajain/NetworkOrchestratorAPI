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
    }
    // 2. TEST GET BY ID
    @Test
    void testGetDeviceById() {
        Device device = new Device();
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        Device result = service.getDeviceById(1L);
        assertNotNull(result);
    }
    // 3. TEST DEVICE NOT FOUND
    @Test
    void testDeviceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getDeviceById(1L));
        assertEquals("Device not found", ex.getMessage());
    }
    // 4. TEST CREATE
    @Test
    void testCreateDevice() {
        Device device = new Device();
        device.setName("Router");
        when(repository.save(device)).thenReturn(device);
        Device result = service.createDevice(device);
        assertEquals("Router", result.getName());
    }
    // 5. TEST DELETE
    @Test
    void testDeleteDevice() {
        doNothing().when(repository).deleteById(1L);
        service.deleteDevice(1L);
        verify(repository, times(1)).deleteById(1L);
    }
    // 6. TEST RESTART LOGIC
    @Test
    void testRestartDevice() {
        Device device = new Device();
        device.setStatus("ACTIVE");
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        Device result = service.restartDevice(1L);
        assertEquals("ACTIVE", result.getStatus());
    }
    // 7. TEST FILTER BY LOCATION
    @Test
    void testGetByLocation() {
        when(repository.findByLocation("office"))
                .thenReturn(List.of(new Device()));
        List<Device> result = service.getDevicesByLocation("office");
        assertEquals(1, result.size());
    }
}