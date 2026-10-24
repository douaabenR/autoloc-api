package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final IEmployeService employeService;

    @PostMapping
    public ResponseEntity<Employe> create(@RequestBody Employe employe) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeService.create(employe));
    }

    @GetMapping("/{id}")
    public Employe findById(@PathVariable("id") Long id) {
        return employeService.findById(id);
    }

    @GetMapping
    public List<Employe> findAll() {
        return employeService.findAll();
    }

    @PutMapping("/{id}")
    public Employe update(@PathVariable("id") Long id, @RequestBody Employe employe) {
        return employeService.update(id, employe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
