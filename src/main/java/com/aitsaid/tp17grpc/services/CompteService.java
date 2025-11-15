package com.aitsaid.tp17grpc.services;

import com.aitsaid.tp17grpc.entities.Compte;
import com.aitsaid.tp17grpc.repositories.CompteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author radouane
 **/
@Service
public class CompteService {

    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public List<Compte> findAllComptes() {
        return compteRepository.findAll();
    }

    public Compte findCompteById(String id) {
        return compteRepository.findById(id).orElse(null);
    }

    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }
}
