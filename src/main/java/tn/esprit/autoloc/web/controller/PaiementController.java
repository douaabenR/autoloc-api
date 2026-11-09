package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementResponseDto;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {
    private final IPaiementService paiementService;
    private final PaiementMapper paiementMapper;

    @GetMapping("/{id}")
    public PaiementResponseDto findById(@PathVariable Long id) {
        return paiementMapper.toDto(paiementService.findById(id));
    }

    @GetMapping
    public List<PaiementResponseDto> findAll() {
        return paiementService.findAll().stream().map(paiementMapper::toDto).toList();
    }
}
