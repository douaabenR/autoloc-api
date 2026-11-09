package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeRequestDto;
import tn.esprit.autoloc.web.dto.EmployeResponseDto;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {
    private final IEmployeService employeService;
    private final EmployeMapper employeMapper;

    @PostMapping
    public ResponseEntity<EmployeResponseDto> create(@Valid @RequestBody EmployeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeMapper.toDto(employeService.create(employeMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public EmployeResponseDto findById(@PathVariable Long id) {
        return employeMapper.toDto(employeService.findById(id));
    }

    @GetMapping
    public List<EmployeResponseDto> findAll() {
        return employeService.findAll().stream().map(employeMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public EmployeResponseDto update(@PathVariable Long id, @Valid @RequestBody EmployeRequestDto dto) {
        return employeMapper.toDto(employeService.update(id, employeMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
