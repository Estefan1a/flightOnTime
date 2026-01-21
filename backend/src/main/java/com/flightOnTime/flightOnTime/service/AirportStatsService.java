package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.AirportStatsDTO;
import com.flightOnTime.flightOnTime.repository.AirportStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportStatsService {

    private final AirportStatsRepository airportStatsRepository;

    public List<AirportStatsDTO> getTop5Airports() {
        // Pageable para top 5
        PageRequest top5 = PageRequest.of(0, 5);
        return airportStatsRepository.getTopProblematicAirports(top5);
    }
}