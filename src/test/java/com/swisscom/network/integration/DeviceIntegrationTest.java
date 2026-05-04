package com.swisscom.network.integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.network.model.Device;
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
class DeviceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void fullFlowTest() throws Exception {
        // 1. CREATE DEVICE
        Device device = new Device();
        device.setName("Router-1");
        device.setDeviceType("router");
        device.setLocation("datacenter");
        device.setIpAddress("192.168.1.1");
        device.setStatus("ACTIVE");
        String response = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("POST Response: " + response);
        // Extract ID
        String id = response.replaceAll(".*\"id\":(\\d+).*", "$1");
        System.out.println("Created Device ID: " + id);
        // 2. GET DEVICE
        mockMvc.perform(get("/devices/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Router-1"));
        // 3. RESTART DEVICE
        mockMvc.perform(post("/devices/" + id + "/restart"))
                .andExpect(status().isOk());
        // 4. VERIFY STATUS AFTER RESTART
        mockMvc.perform(get("/devices/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        // 5. DELETE DEVICE
        mockMvc.perform(delete("/devices/" + id))
                .andExpect(status().isOk());
        // 6. VERIFY DELETE
        mockMvc.perform(get("/devices/" + id))
                .andExpect(status().isNotFound());
    }
}