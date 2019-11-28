package ga.patrick.smns.service;

import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.repository.TemperatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final TemperatureRepository temperatureRepository;

    private final GeocodeService geocodeService;

    public List<Temperature> lastInputs(int n) {
        return temperatureRepository.findByOrderByIdDesc(
                PageRequest.of(0, n)
        );
    }

    public Temperature add(Temperature t) {
        t.setLocation(geocodeService.getLocation(t.getLat(), t.getLon()));
        return temperatureRepository.save(t);
    }

    public void clear() {
        temperatureRepository.deleteAll();
    }
}
