package ga.patrick.smns.service;

import ga.patrick.smns.domain.*;
import ga.patrick.smns.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TemperatureService {

    private TemperatureRepository temperatureRepository;

    public List<Temperature> lastInputs(int n) {
        return temperatureRepository.findByOrderByIdDesc(
                PageRequest.of(0, n)
        );
    }

    public Temperature add(Temperature t) {
        return temperatureRepository.save(t);
    }
}
