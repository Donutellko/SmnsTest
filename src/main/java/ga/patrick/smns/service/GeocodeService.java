package ga.patrick.smns.service;

import ga.patrick.smns.geocode.GeocodeClient;
import ga.patrick.smns.geocode.GeocodeResponse;
import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult;
import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult.AddressComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeocodeService {

    final GeocodeClient geocodeClient;

    public String getCityName(Double lat, Double lon) {
        GeocodeResponse response = decode(lat, lon);
        GeocodeResult[] results = response.getResults();

        String city = findComponentName(results, AddressComponent.CITY_LEVEL);
        String country = findComponentName(results, AddressComponent.COUNTRY_LEVEL);

        return String.format("%s, %s", country, city);
    }

    private String findComponentName(GeocodeResult[] results, String type) {
        for (GeocodeResult result : results) {
            AddressComponent[] components = result.getAddressComponents();
            for (AddressComponent component : components) {
                for (String comp_type : component.getTypes()) {
                    if (comp_type.equals(type)) {
                        return component.getLongName();
                    }
                }
            }
        }
        return null;
    }


    private GeocodeResponse decode(Double lat, Double lon) {
        return geocodeClient.decodeCityLevel(lat, lon);
    }

}
