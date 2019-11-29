package ga.patrick.smns.api;

import ga.patrick.smns.domain.*;
import ga.patrick.smns.dto.ModelMapper;
import ga.patrick.smns.dto.TemperatureDto;
import ga.patrick.smns.service.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class ApiController {

    private TemperatureService temperatureService;

    private ModelMapper modelMapper;

    /**
     * Retrieve latest responses from database. Ordered by descending ID (new to old)
     * @param n max count of elements to retrieve, 10 by default.
     */
    @GetMapping("/latest")
    List<TemperatureDto> latestEntries(
            @RequestParam(name = "count", defaultValue = "10") int n,
            @RequestParam(name = "filter", defaultValue = "") String filter
    ) {
        return temperatureService.lastInputs(n, filter).stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Add new input into database.
     * @return an object stored into database, so client can see ID and other generated data.
     */
    @PostMapping("/add")
    TemperatureDto add(
            @Validated @RequestBody TemperatureDto temp
    ) {
        Temperature added = temperatureService.add(modelMapper.toEntity(temp));

        return modelMapper.toDto(added);
    }

}
