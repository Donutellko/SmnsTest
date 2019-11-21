package ga.patrick.smns.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import ga.patrick.smns.validator.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Temperature {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    @TemperatureConstraint
    private double temperature;

    @Column(nullable = false)
    @LatitudeConstraint
    private double lat;

    @Column(nullable = false)
    @LongitudeConstraint
    private double lon;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(nullable = false)
    private LocalDateTime datetime = LocalDateTime.now();

    public Temperature(double lat, double lon, double temperature) {
        this.lat = lat;
        this.lon = lon;
        this.temperature = temperature;
    }
}
