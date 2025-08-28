package ashteam.farm_leftover.geocoding.service;

import ashteam.farm_leftover.geocoding.dto.exception.GeocodeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingService {

    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    @Value("${google.api.key}")
    private String apiKey;

    public double[] getCoordinates(String city, String street) {
        try {
            String address = street + ", " + city + ", Israel";
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = GEOCODE_URL + "?address=" + encodedAddress +
                    "&components=country:IL&key=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());

            if ("OK".equals(rootNode.get("status").asText())) {
                JsonNode results = rootNode.get("results");
                if (!results.isEmpty()) {
                    JsonNode location = results.get(0).get("geometry").get("location");
                    double lat = location.get("lat").asDouble();
                    double lng = location.get("lng").asDouble();
                    return new double[]{lat, lng};
                }
            }   else if ("ZERO_RESULTS".equals(rootNode.get("status").asText())) {
                return new double[]{31.0461,34.8516};
            }

            String errorMessage = "Geocoding failed";
            if (rootNode.has("status")) {
                errorMessage += ". Status: " + rootNode.get("status").asText();
            }
            if (rootNode.has("error_message")) {
                errorMessage += ". Error: " + rootNode.get("error_message").asText();
            }
            throw new RuntimeException(errorMessage);

        } catch (Exception e) {
            throw new GeocodeException("Geocoding failed for: " + street + ", " + city);
        }
    }
}