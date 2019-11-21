package ga.patrick.smns.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import ga.patrick.smns.domain.Temperature;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TemperatureDto {

    @Null
    private
    Long id;

    @NotNull
    private
    Double lat;

    @NotNull
    private
    Double lon;

    @NotNull
    private
    Double value;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    public Temperature toEntity() {
        Temperature t = new Temperature(value, lat, lon);
        if (time != null) t.setDatetime(time);
        if (id != null) t.setId(id);
        return t;
    }

    public TemperatureDto(Temperature entity) {
        this.id = entity.getId();
        this.lat = entity.getLat();
        this.lon = entity.getLon();
        this.value = entity.getTemperature();
        this.time = entity.getDatetime();
    }
}
