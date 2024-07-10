package org.example.geodata.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class HouseCollectionDTO {

    private final String type = "FeatureCollection";

    private final List<HouseDTO> features;

}
