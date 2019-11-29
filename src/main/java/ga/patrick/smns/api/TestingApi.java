package ga.patrick.smns.api;

import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.dto.ModelMapper;
import ga.patrick.smns.dto.TemperatureDto;
import ga.patrick.smns.service.TemperatureService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for demonstration and testing purposes.
 */
@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestingApi {

    TemperatureService temperatureService;

    private ModelMapper modelMapper;

    private double getRand(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    /**
     * Generate inputs for testing and demonstration purposes.
     * @param count number of inputs to generate.
     * @return generated inputs, as they are stored.
     */
    @RequestMapping("/populate")
    List<TemperatureDto> populate(
            @RequestParam(name = "count", defaultValue = "20") int count
    ) {
        List<Temperature> added = new ArrayList<>();
        for (int i = 0; i < count; i++) {
//            Temperature t = new Temperature(
//                    (int) (100 * getRand(-273, 500)) / 100d,
//                    getRand(-90, 90),
//                    getRand(-180, 180)
//            );
            Temperature t = new Temperature(
                    (int) (100 * getRand(-273, 500)) / 100d,
                    getRand(59.5, 60.5), // lat
                    getRand(29.0, 30.5)  // lon
            );
            added.add(temperatureService.add(t));
        }
        return added.stream().map(modelMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Remove all contents of database.
     * @return JSON object with success = true
     */
    @RequestMapping("/clear")
    Map<String, Object> clear() {
        temperatureService.clear();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cleared.");
        return response;
    }

}
