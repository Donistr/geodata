package org.example.geodata.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.geodata.message.HouseCollectionDTO;
import org.example.geodata.message.HouseDTO;
import org.example.geodata.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping("/all")
    public ResponseEntity<HouseCollectionDTO> getAll() {
        return ResponseEntity.ok(houseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseDTO> getById(@PathVariable @NotBlank String id) {
        return ResponseEntity.ok(houseService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HouseDTO> createHouse(@Valid @RequestBody HouseDTO houseDTO) {
        return ResponseEntity.ok(houseService.create(houseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HouseDTO> updateHouse(@PathVariable @NotBlank String id, @Valid @RequestBody HouseDTO houseDTO) {
        houseDTO.getProperties().setId(id);
        return ResponseEntity.ok(houseService.update(houseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable @NotBlank String id) {
        houseService.delete(id);
        return ResponseEntity.ok().build();
    }

}
