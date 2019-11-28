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
    private Long id;

    /**
     * Temperature.
     * Values are in Celsius, minimal value is -273.15.
     */
    @Column(nullable = false)
    @TemperatureConstraint
    private Double temperature;

    /** Latitude of measurement . Value is between -90 and 90. */
    @Column(nullable = false)
    @LatitudeConstraint
    private Double lat;

    /** Longitude of measurement. Value is between -180 and 180. */
    @Column(nullable = false)
    @LongitudeConstraint
    private Double lon;

    /** City and country of measurement */
    @Column
    private String location;

    @Column(nullable = false)
    private LocalDateTime datetime = LocalDateTime.now();

    public Temperature(double temperature, double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.temperature = temperature;
    }
}
