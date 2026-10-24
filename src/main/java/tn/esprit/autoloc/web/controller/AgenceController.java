package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;

    @PostMapping
    public ResponseEntity<Agence> create(@RequestBody Agence agence) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agenceService.create(agence));
    }

    @GetMapping("/{id}")
    public Agence findById(@PathVariable("id") Long id) {
        return agenceService.findById(id);
    }

    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @PutMapping("/{id}")
    public Agence update(@PathVariable("id") Long id, @RequestBody Agence agence) {
        return agenceService.update(id, agence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
