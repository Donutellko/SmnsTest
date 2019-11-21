package ga.patrick.smns.domain;

import ga.patrick.smns.validator.LatitudeConstraint;
import ga.patrick.smns.validator.LongitudeConstraint;
import ga.patrick.smns.validator.TemperatureConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @TemperatureConstraint
    private double temperature;

    /** Latitude of sensor. Value is between -90 and 90. */
    @Column(nullable = false)
    @LatitudeConstraint
    private double lat;

    /** Longitude of sensor. Value is between -180 and 180. */
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
