package org.example.geodata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("houses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class House {

    @Id
    @Field("id")
    private ObjectId id;

    @Field("street")
    private String street;

    @Field("number")
    private Integer number;

    @Field("location")
    private GeoJsonPolygon location;

}
