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

    @GetMapping("/latest")
    List<TemperatureDto> latestEntries(
            @RequestParam(name = "count", defaultValue = "10") int n
    ) {
        return temperatureService.lastInputs(n).stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    long add(
            @Validated @RequestBody TemperatureDto temp
    ) {
        Temperature added = temperatureService.add(modelMapper.toEntity(temp));
        return added.getId();
    }

}
