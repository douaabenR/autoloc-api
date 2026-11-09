package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementRequestDto;
import tn.esprit.autoloc.web.dto.EquipementResponseDto;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {
    private final IEquipementService equipementService;
    private final EquipementMapper equipementMapper;

    @PostMapping
    public ResponseEntity<EquipementResponseDto> create(@Valid @RequestBody EquipementRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementMapper.toDto(equipementService.create(equipementMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public EquipementResponseDto findById(@PathVariable Long id) {
        return equipementMapper.toDto(equipementService.findById(id));
    }

    @GetMapping
    public List<EquipementResponseDto> findAll() {
        return equipementService.findAll().stream().map(equipementMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public EquipementResponseDto update(@PathVariable Long id, @Valid @RequestBody EquipementRequestDto dto) {
        return equipementMapper.toDto(equipementService.update(id, equipementMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
