package org.example.geodata.mapper.impl;

import org.bson.types.ObjectId;
import org.example.geodata.message.HouseDTO;
import org.example.geodata.entity.House;
import org.example.geodata.mapper.HouseMapper;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HouseMapperImpl implements HouseMapper {

    @Override
    public House map(HouseDTO houseDTO) {
        GeoJsonPolygon location = null;
        ObjectId id = null;
        String street = null;
        Integer number = null;

        if (houseDTO.getGeometry() != null && houseDTO.getGeometry().getCoordinates() != null) {
            location = new GeoJsonPolygon(houseDTO.getGeometry().getCoordinates()
                    .stream()
                    .map(point -> new Point(point.get(0), point.get(1)))
                    .toList());
        }

        HouseDTO.Properties properties = houseDTO.getProperties();
        if (properties != null) {
            if (properties.getId() != null) {
                id = new ObjectId(properties.getId());
            }
            street = properties.getStreet();
            number = properties.getNumber();
        }

        return new House(id, street, number, location);
    }

    @Override
    public HouseDTO map(House house) {
        HouseDTO.Geometry geometry = null;
        HouseDTO.Properties properties = null;

        if (house.getLocation() != null && !house.getLocation().getPoints().isEmpty()) {
            geometry = new HouseDTO.Geometry(HouseDTO.GEOMETRY_GEO_JSON_TYPE, house.getLocation().getPoints()
                    .stream()
                    .map(point -> List.of(point.getX(), point.getY()))
                    .toList());
        }

        if (house.getId() != null || house.getStreet() != null || house.getNumber() != null) {
            properties = new HouseDTO.Properties();
            properties.setId(house.getId().toHexString());
            properties.setStreet(house.getStreet());
            properties.setNumber(house.getNumber());
        }

        return new HouseDTO(HouseDTO.BASE_GEO_JSON_TYPE, geometry, properties);
    }

}
