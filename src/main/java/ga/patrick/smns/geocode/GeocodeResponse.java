package ga.patrick.smns.geocode;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Description of fields:
 * <a href="https://developers.google.com/maps/documentation/geocoding/intro?hl=en_US#reverse-response">
 * Google API documentation
 * </a>
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeResponse {

    String status;

    @JsonAlias("plus_code")
    PlusCode plusCode;

    GeocodeResult[] results;

    @Data
    public static class GeocodeResult {

        @JsonAlias("address_components")
        AddressComponent[] addressComponents;

        @JsonAlias("formatted_address")
        String formattedAddress;

        Geometry geometry;

        @Data
        public static class AddressComponent {

            @JsonAlias("long_name")
            String longName;

            @JsonAlias("short_name")
            String shortName;

            /** Types, such as "administrative_area_level_1", "political", "country"... */
            String[] types;

            public enum ComponentType {
                CITY_LEVEL ("administrative_area_level_1"),
                COUNTRY_LEVEL ("country"),
                POLITICAL ("political");

                final String type;

                public String getName() {
                    return type;
                }

                ComponentType(String name) {
                    this.type = name;
                }
            }
        }

        @Data
        public static class Geometry {

            Bounds bounds;

            @Data
            public static class Bounds {
                Coordinate northeast;
                Coordinate southwest;

                @Data
                public static class Coordinate {
                    double lat;
                    double lng;
                }
            }
        }
    }

    @Data
    private static class PlusCode {

        @JsonAlias("compound_code")
        String compoundCode;

        @JsonAlias("global_code")
        String globalCode;

    }
}
