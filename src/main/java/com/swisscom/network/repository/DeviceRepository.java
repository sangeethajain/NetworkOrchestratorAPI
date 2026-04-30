package com.swisscom.network.repository;

import com.swisscom.network.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByLocation(String location);

    List<Device> findByStatus(String status);
}