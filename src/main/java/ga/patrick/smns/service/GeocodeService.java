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
                    if (type.getName().equals(comp_type)) {
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
        Location location = locationRepository.find(lat, lon);
        if (location == null) {
            GeocodeResponse response = geocodeClient.decodeCityLevel(lat, lon);
            Bounds bounds = getBounds(response, CITY_LEVEL);
            GeocodeResult result = findResult(response, CITY_LEVEL);

            if (result != null && bounds != null) {
                location = new Location();

                location.setTop(bounds.getNortheast().getLat());
                location.setRight(bounds.getNortheast().getLng());
                location.setBottom(bounds.getSouthwest().getLat());
                location.setLeft(bounds.getSouthwest().getLng());

                location.setName(result.getFormattedAddress());

                location = locationRepository.save(location);
            }
        }
        return location;
    }
}
