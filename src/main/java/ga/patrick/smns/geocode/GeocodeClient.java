package ga.patrick.smns.geocode;

import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult.AddressComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GeocodeClient {

    final GeocodeConfig geocodeConfig;

    private static String GOOGLE_GEOCODE_URL =
            "https://maps.googleapis.com/maps/api/geocode/json";

    private static String RESULT_TYPE_FILTER = "result_type";

    // https://maps.googleapis.com/maps/api/geocode/json?result_type=administrative_area_level_1&latlng=40.71,-73.96&key=******
    public GeocodeResponse decodeCityLevel(Double lat, Double lon) {
        String CITY_LEVEL_FILTER = RESULT_TYPE_FILTER + "=" + AddressComponent.CITY_LEVEL;
        String url = String.format("%s?%s&latlng=%s,%s&key=%s",
                GOOGLE_GEOCODE_URL,
                CITY_LEVEL_FILTER,
                lat, lon, geocodeConfig.getApiKey());

        return geocode(url);
    }

    public GeocodeResponse geocode(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GeocodeResponse> response
                = restTemplate.getForEntity(url, GeocodeResponse.class);
        return response.getBody();
    }


}
