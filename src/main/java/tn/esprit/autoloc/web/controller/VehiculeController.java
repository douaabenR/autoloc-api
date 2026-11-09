package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeRequestDto;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {
    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    @PostMapping
    public ResponseEntity<VehiculeResponseDto> create(@Valid @RequestBody VehiculeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeMapper.toDto(vehiculeService.create(vehiculeMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public VehiculeResponseDto findById(@PathVariable Long id) {
        return vehiculeMapper.toDto(vehiculeService.findById(id));
    }

    @GetMapping
    public List<VehiculeResponseDto> findAll() {
        return vehiculeService.findAll().stream().map(vehiculeMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public VehiculeResponseDto update(@PathVariable Long id, @Valid @RequestBody VehiculeRequestDto dto) {
        return vehiculeMapper.toDto(vehiculeService.update(id, vehiculeMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
