package ga.patrick.smns.dto;

import ga.patrick.smns.domain.Temperature;
import org.springframework.stereotype.Component;

public class ModelMapper {

    public Temperature toEntity(TemperatureDto dto) {
        Temperature entity = new Temperature(dto.getValue(), dto.getLat(), dto.getLon());
        if (dto.getTime() != null) entity.setDatetime(dto.getTime());
        if (dto.getId() != null) entity.setId(dto.getId());
        return entity;
    }

    public TemperatureDto toDto(Temperature entity) {
        TemperatureDto dto = new TemperatureDto();
        dto.setId(entity.getId());
        dto.setLat(entity.getLat());
        dto.setLon(entity.getLon());
        dto.setValue(entity.getTemperature());
        dto.setTime(entity.getDatetime());
        return dto;
    }

}
