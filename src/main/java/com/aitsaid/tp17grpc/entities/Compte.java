package com.aitsaid.tp17grpc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author radouane
 **/
@Entity
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private float solde;
    private String dateCreation;
    private String type;

    public Compte() {

    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public float getSolde() { return solde; }
    public void setSolde(float solde) { this.solde = solde; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
