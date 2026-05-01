package com.swisscom.network.service;
import com.swisscom.network.model.Device;
import com.swisscom.network.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceService {
    private final DeviceRepository repository;
    public DeviceService(DeviceRepository repository) {
        this.repository = repository;
    }
    public List<Device> getAllDevices() {
        return repository.findAll();
    }
    public Device getDeviceById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }
    public Device createDevice(Device device) {
        device.setStatus("ACTIVE"); // default behavior
        return repository.save(device);
    }
    public Device updateDevice(Long id, Device updated) {
        Device device = getDeviceById(id);
        device.setName(updated.getName());
        device.setDeviceType(updated.getDeviceType());
        device.setLocation(updated.getLocation());
        device.setIpAddress(updated.getIpAddress());
        device.setStatus(updated.getStatus());
        return repository.save(device);
    }
    public void deleteDevice(Long id) {
        repository.deleteById(id);
    }
    public List<Device> getDevicesByLocation(String location) {
        return repository.findByLocation(location);
    }
    public List<Device> getDevicesByStatus(String status) {
        return repository.findByStatus(status);
    }
    // Network simulation logic
    @Transactional
    public Device restartDevice(Long id) {
        Device device = getDeviceById(id);
        if(!device.getStatus().equals("ACTIVE")){
            throw new RuntimeException("Device is not active, cannot restart");
        }
        device.setStatus("RESTARTING");
        System.out.println("Status now: RESTARTING");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // simulate restart
        device.setStatus("ACTIVE");
        System.out.println("Status now: ACTIVE");
        return device;

    }
}