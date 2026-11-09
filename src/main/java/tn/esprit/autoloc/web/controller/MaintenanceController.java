package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceRequestDto;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDto;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {
    private final IMaintenanceService maintenanceService;
    private final MaintenanceMapper maintenanceMapper;

    @PostMapping
    public ResponseEntity<MaintenanceResponseDto> create(@Valid @RequestBody MaintenanceRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceMapper.toDto(maintenanceService.create(maintenanceMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public MaintenanceResponseDto findById(@PathVariable Long id) {
        return maintenanceMapper.toDto(maintenanceService.findById(id));
    }

    @GetMapping
    public List<MaintenanceResponseDto> findAll() {
        return maintenanceService.findAll().stream().map(maintenanceMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public MaintenanceResponseDto update(@PathVariable Long id, @Valid @RequestBody MaintenanceRequestDto dto) {
        return maintenanceMapper.toDto(maintenanceService.update(id, maintenanceMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
