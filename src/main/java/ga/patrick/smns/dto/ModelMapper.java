package ga.patrick.smns.dto;

import ga.patrick.smns.domain.Temperature;

public class ModelMapper {

    public Temperature toEntity(TemperatureDto dto) {
        Temperature entity = new Temperature(dto.getValue(), dto.getLat(), dto.getLon());
        if (dto.getTime() != null) entity.setDatetime(dto.getTime());
        if (dto.getId() != null) entity.setId(dto.getId());
        if (dto.getLocation() != null) entity.setLocation(dto.getLocation());
        return entity;
    }

    public TemperatureDto toDto(Temperature entity) {
        TemperatureDto dto = new TemperatureDto();
        dto.setId(entity.getId());
        dto.setLat(entity.getLat());
        dto.setLon(entity.getLon());
        dto.setValue(entity.getTemperature());
        dto.setTime(entity.getDatetime());
        dto.setLocation(entity.getLocation());
        return dto;
    }

}
