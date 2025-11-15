package com.aitsaid.tp17grpc.repositories;

import com.aitsaid.tp17grpc.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author radouane
 **/
public interface CompteRepository extends JpaRepository<Compte, String> {
}
