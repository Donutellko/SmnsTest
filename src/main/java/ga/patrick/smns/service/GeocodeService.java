package ga.patrick.smns.service;

import ga.patrick.smns.domain.Location;
import ga.patrick.smns.geocode.GeocodeClient;
import ga.patrick.smns.geocode.GeocodeResponse;
import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult;
import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult.AddressComponent;
import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult.AddressComponent.ComponentType;
import ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult.Geometry.Bounds;
import ga.patrick.smns.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ga.patrick.smns.geocode.GeocodeResponse.GeocodeResult.AddressComponent.ComponentType.CITY_LEVEL;

@Service
@RequiredArgsConstructor
public class GeocodeService {

    final GeocodeClient geocodeClient;

    final LocationRepository locationRepository;

    /** Get component for an area of specified type */
    @SuppressWarnings("SameParameterValue")
    private AddressComponent findComponent(GeocodeResponse response, ComponentType type) {
        for (GeocodeResult result : response.getResults()) {
            for (AddressComponent component : result.getAddressComponents()) {
                for (String comp_type : component.getTypes()) {
                    if (type.toString().equals(comp_type)) {
                        return component;
                    }
                }
            }
        }
        return null;
    }

    /** Find result contatining component of specified type */
    private GeocodeResult findResult(GeocodeResponse response, ComponentType type) {
        for (GeocodeResult result : response.getResults()) {
            if (findComponent(response, type) != null) {
                return result;
            }
        }
        return null;
    }

    /** Get bounds for an area of specified type */
    @SuppressWarnings("SameParameterValue")
    private Bounds getBounds(GeocodeResponse response, ComponentType type) {
        GeocodeResult result = findResult(response, type);
        if (result != null && result.getGeometry() != null) {
            return result.getGeometry().getBounds();
        }
        return null;
    }

    public Location getLocation(Double lat, Double lon) {
        Location location = locationRepository.findTopBy(lat, lon);
        if (location == null) {
            GeocodeResponse response = geocodeClient.decodeCityLevel(lat, lon);
            Bounds bounds = getBounds(response, CITY_LEVEL);
            GeocodeResult result = findResult(response, CITY_LEVEL);

            if (result != null && bounds != null) {
                location = new Location(
                        result.getFormattedAddress(),
                        bounds.getNortheast().getLat(), // top
                        bounds.getNortheast().getLng(), // right
                        bounds.getSouthwest().getLat(), // bottom
                        bounds.getSouthwest().getLng()  // left
                );
            } else {
                double top = (int) (lat * 10) / 10.0;
                double left = (int) (lon * 10) / 10.0;
                location = new Location(
                        "Empty place",
                        top + 0.1, left + 0.1,
                        top - 0.1, left - 0.1);
            }
            location = locationRepository.save(location);
        }
        return location;
    }
}
