package com.flightOnTime.flightOnTime.repository;

import com.flightOnTime.flightOnTime.entity.FlightRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRequestRespository extends JpaRepository<FlightRequest, Long> {
}
