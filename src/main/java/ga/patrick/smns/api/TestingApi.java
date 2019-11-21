package ga.patrick.smns.api;

import ga.patrick.smns.domain.Temperature;
import ga.patrick.smns.service.TemperatureService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for demonstration and testing purposes.
 */
@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestingApi {

    TemperatureService temperatureService;

    private double getRand(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    @RequestMapping("/populate")
    List<Temperature> populate(
            @RequestParam(name = "count", defaultValue = "20") int count
    ) {
        List<Temperature> added = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Temperature t = new     Temperature(
                    getRand(-90, 90),
                    getRand(-180, 180),
                    getRand(-273, 500)
            );
            added.add(temperatureService.add(t));
        }
        return added;
    }

    @RequestMapping("/clear")
    String clear() {
        temperatureService.clear();
        return "Done!";
    }

}
