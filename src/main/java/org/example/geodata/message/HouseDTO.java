package org.example.geodata.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseDTO {

    public static final String BASE_GEO_JSON_TYPE = "Feature";

    public static final String GEOMETRY_GEO_JSON_TYPE = "Polygon";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Geometry {

        @Pattern(regexp = GEOMETRY_GEO_JSON_TYPE)
        private String type;

        private List<@Size(min = 2, max = 2) List<Double>> coordinates;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Properties {

        private String id;

        private String street;

        private Integer number;

    }

    @Pattern(regexp = BASE_GEO_JSON_TYPE)
    private String type;

    @Valid
    private Geometry geometry;

    private Properties properties;

}
