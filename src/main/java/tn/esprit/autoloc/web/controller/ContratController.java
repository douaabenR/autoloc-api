package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratRequestDto;
import tn.esprit.autoloc.web.dto.ContratResponseDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
public class ContratController {
    private final IContratService contratService;
    private final ContratMapper contratMapper;

    @PostMapping
    public ResponseEntity<ContratResponseDto> create(@Valid @RequestBody ContratRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratMapper.toDto(contratService.create(contratMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public ContratResponseDto findById(@PathVariable Long id) {
        return contratMapper.toDto(contratService.findById(id));
    }

    @GetMapping
    public List<ContratResponseDto> findAll() {
        return contratService.findAll().stream().map(contratMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ContratResponseDto update(@PathVariable Long id, @Valid @RequestBody ContratRequestDto dto) {
        return contratMapper.toDto(contratService.update(id, contratMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
