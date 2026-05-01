package com.swisscom.network.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.network.model.Device;
import com.swisscom.network.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DeviceRepository repository;
    // 1. TEST CREATE DEVICE
    @Test
    void testCreateDevice() throws Exception {
        Device device = new Device();
        device.setName("Router1");
        device.setDeviceType("router");
        device.setLocation("office");
        device.setIpAddress("192.168.1.1");
        device.setStatus("ACTIVE");
        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Router1"));
        // print DB state
        System.out.println("Devices in DB: " + repository.findAll());
    }
    //2. TEST GET ALL
    @Test
    void testGetAllDevices() throws Exception {
        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    // 3. TEST GET BY LOCATION
    @Test
    void testGetByLocation() throws Exception {
        mockMvc.perform(get("/devices/location/office"))
                .andExpect(status().isOk());
    }
    // 4. TEST RESTART
    @Test
    void testRestartDevice() throws Exception {
        // First create device
        Device device = new Device();
        device.setName("Switch1");
        device.setDeviceType("switch");
        device.setLocation("datacenter");
        device.setIpAddress("10.0.0.1");
        device.setStatus("ACTIVE");
        Device saved = repository.save(device);
        mockMvc.perform(post("/devices/" + saved.getId() + "/restart"))
                .andExpect(status().isOk());

        System.out.println("Restart called for ID: " + saved.getId());
    }
    // 5. TEST DELETE
    @Test
    void testDeleteDevice() throws Exception {
        Device device = new Device();
        device.setName("DeleteTest");
        device.setDeviceType("router");
        device.setLocation("office");
        device.setIpAddress("1.1.1.1");
        device.setStatus("ACTIVE");
        Device saved = repository.save(device);
        mockMvc.perform(delete("/devices/" + saved.getId()))
                .andExpect(status().isOk());
        System.out.println("Deleted ID: " + saved.getId());
    }
}