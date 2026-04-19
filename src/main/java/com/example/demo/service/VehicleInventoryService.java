package com.example.demo.service;

import com.example.demo.dto.VehicleDto;
import com.example.demo.model.InventoryVehicle;
import com.example.demo.repository.InventoryVehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleInventoryService {

    private final InventoryVehicleRepository repository;

    @Value("${app.inventory.demo-seed:true}")
    private boolean demoSeedEnabled;

    public VehicleInventoryService(InventoryVehicleRepository repository) {
        this.repository = repository;
    }

    /**
     * If enabled and this dealer has no rows yet, create a unique synthetic lineup
     * (deterministic per {@code placeId}). For production data, disable demo-seed and insert real rows.
     */
    @Transactional
    public void ensureDemoInventoryForPlaceIfConfigured(String placeId) {
        if (!demoSeedEnabled || placeId == null || placeId.isBlank()) {
            return;
        }
        if (repository.countByDealerPlaceId(placeId) > 0) {
            return;
        }
        repository.saveAll(DemoInventorySeeder.synthesizeForPlaceId(placeId));
    }

    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesForDealer(String dealerId) {
        if (dealerId == null || dealerId.isBlank()) {
            return List.of();
        }
        return repository.findByDealerPlaceIdOrderByIdAsc(dealerId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public int getVehicleCountForDealer(String dealerId) {
        if (dealerId == null || dealerId.isBlank()) {
            return 0;
        }
        return (int) repository.countByDealerPlaceId(dealerId);
    }

    private VehicleDto toDto(InventoryVehicle v) {
        return new VehicleDto(
                v.getId(),
                v.getYear(),
                v.getMake(),
                v.getModel(),
                v.getPrice(),
                v.getMileage(),
                v.getConditionType(),
                v.getImageUrl(),
                v.getVin()
        );
    }
}
