package com.example.demo.service;

import com.example.demo.model.Dealer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GooglePlacesService {

    private static final Logger log = LoggerFactory.getLogger(GooglePlacesService.class);

    @Autowired
    private VehicleInventoryService vehicleInventoryService;

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Value("${app.places.details.enabled:true}")
    private boolean placeDetailsEnabled;

    /** Limit extra Place Details calls (billing / latency). */
    @Value("${app.places.details.max-dealers:15}")
    private int maxPlaceDetails;

    public List<Dealer> getDealers(double lat, double lng) {
        RestTemplate restTemplate = new RestTemplate();
        List<Dealer> dealers = new ArrayList<>();
        String nextPageToken = null;

        do {
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                    + "?location=" + lat + "," + lng
                    + "&radius=25000"
                    + "&keyword=car%20dealer"
                    + "&key=" + apiKey
                    + (nextPageToken != null ? "&pagetoken=" + UriUtils.encodeQueryParam(nextPageToken, StandardCharsets.UTF_8) : "");

            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);

            String status = json.optString("status");
            if (!"OK".equals(status) && !"ZERO_RESULTS".equals(status)) {
                log.warn("Places API returned status: {}", status);
                break;
            }

            JSONArray results = json.optJSONArray("results");
            if (results == null) break;

            for (int i = 0; i < results.length(); i++) {
                JSONObject place = results.getJSONObject(i);

                String name = place.getString("name");
                double rating = place.optDouble("rating", 0);
                String address = place.optString("vicinity", "");
                String placeId = place.getString("place_id");

                JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
                double dlat = location.getDouble("lat");
                double dlng = location.getDouble("lng");

                Dealer dealer = new Dealer(name, rating, address, dlat, dlng);
                dealer.setId(placeId);

                vehicleInventoryService.ensureDemoInventoryForPlaceIfConfigured(placeId);
                dealer.setVehicleCount(vehicleInventoryService.getVehicleCountForDealer(placeId));

                if (placeDetailsEnabled && dealers.size() < maxPlaceDetails) {
                    enrichFromPlaceDetails(restTemplate, dealer, placeId);
                }

                if (place.has("photos")) {
                    String photoRef = place.getJSONArray("photos")
                            .getJSONObject(0)
                            .getString("photo_reference");
                    String encodedRef = UriUtils.encodeQueryParam(photoRef, StandardCharsets.UTF_8);
                    dealer.setImage("http://localhost:8080/api/dealers/photo?photoReference=" + encodedRef);
                }

                dealer.setMapUrl("https://www.google.com/maps/search/?api=1&query=" + dlat + "," + dlng);
                dealers.add(dealer);
            }

            nextPageToken = json.optString("next_page_token", null);
            if (nextPageToken != null && !nextPageToken.isBlank()) {
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }

        } while (nextPageToken != null && !nextPageToken.isBlank());

        return dealers;
    }

    private void enrichFromPlaceDetails(RestTemplate restTemplate, Dealer dealer, String placeId) {
        try {
            String encodedId = UriUtils.encodeQueryParam(placeId, StandardCharsets.UTF_8);
            String detailUrl = "https://maps.googleapis.com/maps/api/place/details/json"
                    + "?place_id=" + encodedId
                    + "&fields=formatted_phone_number,international_phone_number,website"
                    + "&key=" + apiKey;

            String body = restTemplate.getForObject(detailUrl, String.class);
            JSONObject root = new JSONObject(body);
            if (!"OK".equals(root.optString("status"))) {
                return;
            }
            JSONObject result = root.getJSONObject("result");

            String formatted = result.optString("formatted_phone_number", "");
            if (!formatted.isBlank()) {
                dealer.setPhone(formatted);
            } else {
                String intl = result.optString("international_phone_number", "");
                if (!intl.isBlank()) {
                    dealer.setPhone(intl);
                }
            }

            String website = result.optString("website", "");
            if (!website.isBlank()) {
                dealer.setWebsite(website);
            }
        } catch (Exception e) {
            log.debug("Place details failed for {}: {}", placeId, e.getMessage());
        }
    }
}
