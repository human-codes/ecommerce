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
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);

            // ✅ Log response for debugging
            log.info("OpenStreetMap API Response: {}", response);

            if (response != null) {
                JSONObject json = new JSONObject(response);
                return json.getString("display_name");
            }
        } catch (Exception e) {
            log.error("Error fetching address from OpenStreetMap: ", e);
        }
        return "Unknown Location";
    }
}
