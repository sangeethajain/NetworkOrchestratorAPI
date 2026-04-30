package com.swisscom.network.controller;

import com.swisscom.network.model.Device;
import com.swisscom.network.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Device> getAll() {
        return service.getAllDevices();
    }

    @GetMapping("/{id}")
    public Device getById(@PathVariable Long id) {
        return service.getDeviceById(id);
    }

    @PostMapping
    public Device create(@RequestBody Device device) {
        return service.createDevice(device);
    }

    @PutMapping("/{id}")
    public Device update(@PathVariable Long id, @RequestBody Device device) {
        return service.updateDevice(id, device);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteDevice(id);
    }

    @GetMapping("/location/{location}")
    public List<Device> getByLocation(@PathVariable String location) {
        return service.getDevicesByLocation(location);
    }

    @GetMapping("/status/{status}")
    public List<Device> getByStatus(@PathVariable String status) {
        return service.getDevicesByStatus(status);
    }

    @PostMapping("/{id}/restart")
    public Device restart(@PathVariable Long id) {
        return service.restartDevice(id);
    }
}
