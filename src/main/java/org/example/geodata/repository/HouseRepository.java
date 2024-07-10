package org.example.geodata.repository;

import org.bson.types.ObjectId;
import org.example.geodata.entity.House;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HouseRepository extends MongoRepository<House, ObjectId> {

    Optional<House> findFirstByStreetAndNumber(String street, Integer number);

}
