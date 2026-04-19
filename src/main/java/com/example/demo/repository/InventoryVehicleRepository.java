package com.example.demo.repository;

import com.example.demo.model.InventoryVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryVehicleRepository extends JpaRepository<InventoryVehicle, Long> {

    List<InventoryVehicle> findByDealerPlaceIdOrderByIdAsc(String dealerPlaceId);

    long countByDealerPlaceId(String dealerPlaceId);
}
