package ga.patrick.smns.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

    /** Temperature.
     * Assuming that all values are in same scale.
     * Minimal values for different scales are: -273.15C, -459.67F and 0K.
     * Thus, valid values must be not less than -459.67.
     */
    @Column(nullable = false)
    private double temperature;

    /** Latitude of sensor. Value is between -90 and 90. */
    @Column(nullable = false)
    private double lat;

    /** Longitude of sensor. Value is between -180 and 180. */
    @Column(nullable = false)
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
