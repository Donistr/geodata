package org.example.geodata.service.impl;

import org.bson.types.ObjectId;
import org.example.geodata.message.HouseCollectionDTO;
import org.example.geodata.message.HouseDTO;
import org.example.geodata.entity.House;
import org.example.geodata.exception.HouseIncorrectDataException;
import org.example.geodata.exception.HouseNotFoundException;
import org.example.geodata.exception.HouseWithSuchIdAlreadyExistsException;
import org.example.geodata.mapper.HouseMapper;
import org.example.geodata.repository.HouseRepository;
import org.example.geodata.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl implements HouseService {

    private final MongoTemplate mongoTemplate;

    private final HouseRepository repository;

    private final HouseMapper mapper;

    @Autowired
    public HouseServiceImpl(MongoTemplate mongoTemplate, HouseRepository repository, HouseMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public HouseCollectionDTO getAll() {
        return new HouseCollectionDTO(
                repository.findAll().stream()
                .map(mapper::map)
                .toList());
    }

    @Override
    public HouseDTO getById(String id) {
        return mapper.map(repository.findById(new ObjectId(id))
                .orElseThrow(() -> new HouseNotFoundException("не найден дом с id " + id)));
    }

    @Override
    public HouseDTO create(HouseDTO houseDTO) {
        House newHouse = mapper.map(houseDTO);
        if (newHouse.getId() != null) {
            throw new HouseWithSuchIdAlreadyExistsException("дом с id " + newHouse.getId() + " уже существует");
        }

        String street = newHouse.getStreet();
        Integer number = newHouse.getNumber();
        if (street == null) {
            throw new HouseIncorrectDataException("не указано название улицы");
        }
        if (number == null) {
            throw new HouseIncorrectDataException("не указан номер дома");
        }
        if (newHouse.getLocation() == null) {
            throw new HouseIncorrectDataException("не указаны координаты дома");
        }

        if (repository.findFirstByStreetAndNumber(street, number).isPresent()) {
            throw new HouseIncorrectDataException("дом с такими улицей и номером дома уже существует");
        }

        if (intersectOtherHousesCount(newHouse.getLocation()) > 0) {
            throw new HouseIncorrectDataException("дом имеет недопустимые границы относительно прочих домов");
        }

        return mapper.map(repository.save(mapper.map(houseDTO)));
    }

    @Override
    public HouseDTO update(HouseDTO houseDTO) {
        House newHouse = mapper.map(houseDTO);
        if (newHouse.getId() == null) {
            throw new HouseWithSuchIdAlreadyExistsException("не указан id");
        }
        House house = repository.findById(newHouse.getId()).
                orElseThrow(() -> new HouseIncorrectDataException("не найден дом с id " + newHouse.getId()));

        String streetPrev = house.getStreet();
        Integer numberPrev = house.getNumber();

        if (newHouse.getStreet() != null) {
            house.setStreet(newHouse.getStreet());
        }
        if (newHouse.getNumber() != null) {
            house.setNumber(newHouse.getNumber());
        }
        if (newHouse.getLocation() != null) {
            house.setLocation(newHouse.getLocation());
        }

        if ((!house.getStreet().equals(streetPrev) || !house.getNumber().equals(numberPrev))
                && repository.findFirstByStreetAndNumber(house.getStreet(), house.getNumber()).isPresent()) {
            throw new HouseIncorrectDataException("дом с такими улицей и номером дома уже существует");
        }

        if (intersectOtherHousesCount(house.getLocation()) > 1) {
            throw new HouseIncorrectDataException("дом имеет недопустимые границы относительно прочих домов");
        }

        return mapper.map(repository.save(house));
    }

    @Override
    public void delete(String id) {
        repository.deleteById(new ObjectId(id));
    }

    private long intersectOtherHousesCount(GeoJsonPolygon newPolygon) {
        Query query = new Query();
        query.addCriteria(Criteria.where("location").intersects(newPolygon));

        return mongoTemplate.count(query, House.class);
    }

}
