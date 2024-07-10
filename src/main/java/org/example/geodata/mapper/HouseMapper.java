package org.example.geodata.mapper;

import org.example.geodata.message.HouseDTO;
import org.example.geodata.entity.House;

public interface HouseMapper {

    House map(HouseDTO houseDTO);

    HouseDTO map(House house);

}
