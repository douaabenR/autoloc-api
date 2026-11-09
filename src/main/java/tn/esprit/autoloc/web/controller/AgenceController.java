package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceRequestDto;
import tn.esprit.autoloc.web.dto.AgenceResponseDto;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

import java.util.List;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
public class AgenceController {
    private final IAgenceService agenceService;
    private final AgenceMapper agenceMapper;

    @PostMapping
    public ResponseEntity<AgenceResponseDto> create(@Valid @RequestBody AgenceRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agenceMapper.toDto(agenceService.create(agenceMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public AgenceResponseDto findById(@PathVariable Long id) {
        return agenceMapper.toDto(agenceService.findById(id));
    }

    @GetMapping
    public List<AgenceResponseDto> findAll() {
        return agenceService.findAll().stream().map(agenceMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public AgenceResponseDto update(@PathVariable Long id, @Valid @RequestBody AgenceRequestDto dto) {
        return agenceMapper.toDto(agenceService.update(id, agenceMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
