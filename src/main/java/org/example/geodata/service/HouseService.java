package org.example.geodata.service;

import org.example.geodata.message.HouseCollectionDTO;
import org.example.geodata.message.HouseDTO;

public interface HouseService {

    HouseCollectionDTO getAll();

    HouseDTO getById(String id);

    HouseDTO create(HouseDTO houseDTO);

    HouseDTO update(HouseDTO houseDTO);

    void delete(String id);

}
