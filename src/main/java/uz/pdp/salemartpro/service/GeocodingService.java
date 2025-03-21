package uz.pdp.salemartpro.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GeocodingService {
    private static final String OSM_URL = "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f";

    public String getFullAddress(Float latitude, Float longitude) {
        String url = String.format(OSM_URL, latitude, longitude);
        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.getForObject(url, String.class);

            log.info("OpenStreetMap API Response: {}", response);

            if (response != null) {
                JSONObject json = new JSONObject(response);
                JSONObject address = json.getJSONObject("address");

                // Extract specific address components
                String city = address.optString("city", address.optString("town", address.optString("village", "")));
                String region = address.optString("state", "");
                String district = address.optString("county", "");
                String street = address.optString("road", "");
                String houseNumber = address.optString("house_number", "");

                // Build a formatted address
                return String.format("%s, %s, %s, %s %s",
                        city, region, district, street, houseNumber).replaceAll(",?\\s+", ", ").replaceAll(", $", "");
            }
        } catch (Exception e) {
            log.error("Error fetching address from OpenStreetMap: ", e);
        }
        return "Unknown Location";
    }
}
