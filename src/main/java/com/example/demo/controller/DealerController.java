package com.example.demo.controller;

import com.example.demo.dto.VehicleDto;
import com.example.demo.model.Dealer;
import com.example.demo.service.GooglePlacesService;
import com.example.demo.service.VehicleInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3000"})
@RestController
@RequestMapping("/api/dealers")
public class DealerController {

    @Autowired
    private GooglePlacesService service;

    @Autowired
    private VehicleInventoryService vehicleInventoryService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping
    public List<Dealer> getDealers(@RequestParam double lat,
                                   @RequestParam double lng) {
        return service.getDealers(lat, lng);
    }

    /**
     * Full vehicle list for one dealer (used by React when user expands inventory).
     * {@code dealerId} is the Google {@code place_id} from {@link Dealer#getId()}.
     */
    @GetMapping("/{dealerId}/vehicles")
    public List<VehicleDto> getVehicles(@PathVariable String dealerId) {
        return vehicleInventoryService.getVehiclesForDealer(dealerId);
    }

    @GetMapping("/photo")
    public ResponseEntity<byte[]> getPhoto(@RequestParam String photoReference) {
        String encoded = UriUtils.encodeQueryParam(photoReference, StandardCharsets.UTF_8);
        String url = "https://maps.googleapis.com/maps/api/place/photo"
                + "?maxwidth=400"
                + "&photo_reference=" + encoded
                + "&key=" + googleMapsApiKey;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(response.getBody());
    }
}
