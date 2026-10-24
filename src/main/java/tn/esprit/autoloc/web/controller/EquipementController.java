package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;

    @PostMapping
    public ResponseEntity<Equipement> create(@RequestBody Equipement equipement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementService.create(equipement));
    }

    @GetMapping("/{id}")
    public Equipement findById(@PathVariable("id") Long id) {
        return equipementService.findById(id);
    }

    @GetMapping
    public List<Equipement> findAll() {
        return equipementService.findAll();
    }

    @PutMapping("/{id}")
    public Equipement update(@PathVariable("id") Long id, @RequestBody Equipement equipement) {
        return equipementService.update(id, equipement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
