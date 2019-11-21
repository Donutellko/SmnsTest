package ga.patrick.smns.api;

import ga.patrick.smns.domain.*;
import ga.patrick.smns.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class ApiController {

    private TemperatureService temperatureService;

    @GetMapping("/latest")
    List<Temperature> latestEntries(
            @RequestParam(name = "count", defaultValue = "10") int n
    ) {
        return temperatureService.lastInputs(n);
    }

    @PostMapping("/add")
    long add(
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lon") double lon,
            @RequestParam(name = "value") double temperature,
            @RequestParam(name = "time", required = false) LocalDateTime datetime
    ) {
        Temperature t = new Temperature(lat, lon, temperature);
        if (datetime != null) t.setDatetime(datetime);
        Temperature added = temperatureService.add(t);
        return added.getId();
    }


}
