package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientRequestDto;
import tn.esprit.autoloc.web.dto.ClientResponseDto;
import tn.esprit.autoloc.web.mapper.ClientMapper;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final IClientService clientService;
    private final ClientMapper clientMapper;

    @PostMapping
    public ResponseEntity<ClientResponseDto> create(@Valid @RequestBody ClientRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientMapper.toDto(clientService.create(clientMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public ClientResponseDto findById(@PathVariable Long id) {
        return clientMapper.toDto(clientService.findById(id));
    }

    @GetMapping
    public List<ClientResponseDto> findAll() {
        return clientService.findAll().stream().map(clientMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ClientResponseDto update(@PathVariable Long id, @Valid @RequestBody ClientRequestDto dto) {
        return clientMapper.toDto(clientService.update(id, clientMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
