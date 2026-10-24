package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;

    @PostMapping
    public ResponseEntity<Contrat> create(@RequestBody Contrat contrat) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(contrat));
    }

    @GetMapping("/{id}")
    public Contrat findById(@PathVariable("id") Long id) {
        return contratService.findById(id);
    }

    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @PutMapping("/{id}")
    public Contrat update(@PathVariable("id") Long id, @RequestBody Contrat contrat) {
        return contratService.update(id, contrat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
