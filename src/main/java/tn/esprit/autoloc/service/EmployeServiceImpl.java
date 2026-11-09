package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;

    @Override
    public Employe create(Employe employe) {
        validate(employe);
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant.");
        }
        employe.setAgence(findAgence(employe));
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        validate(employe);
        Employe existing = findById(id);
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setRole(employe.getRole());
        existing.setAgence(findAgence(employe));
        return employeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employé", id);
        }
        employeRepository.deleteById(id);
    }

    private void validate(Employe employe) {
        if (employe == null || employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire.");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException("Le poste (rôle) de l'employé est obligatoire.");
        }
    }

    private tn.esprit.autoloc.domain.Agence findAgence(Employe employe) {
        if (employe.getAgence() == null || employe.getAgence().getIdAgence() == null) {
            throw new IllegalArgumentException("L'agence de l'employé est obligatoire. Fournissez agenceId.");
        }
        Long id = employe.getAgence().getIdAgence();
        return agenceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }
}
