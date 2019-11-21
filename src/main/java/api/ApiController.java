package api;

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

}
