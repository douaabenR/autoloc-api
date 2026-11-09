package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDto;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final IReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody ReservationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toDto(reservationService.create(reservationMapper.toEntity(dto))));
    }

    @GetMapping("/{id}")
    public ReservationResponseDto findById(@PathVariable Long id) {
        return reservationMapper.toDto(reservationService.findById(id));
    }

    @GetMapping
    public List<ReservationResponseDto> findAll() {
        return reservationService.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ReservationResponseDto update(@PathVariable Long id, @Valid @RequestBody ReservationRequestDto dto) {
        return reservationMapper.toDto(reservationService.update(id, reservationMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
