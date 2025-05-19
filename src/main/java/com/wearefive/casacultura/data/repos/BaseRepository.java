/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.data.repos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author juanje
 */
public class BaseRepository implements AutoCloseable {
    
    private EntityManagerFactory emf;

    public EntityManager em;
    
    private String textoBuscar;
    private Integer maximoResultados;
    
    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public BaseRepository() {
        this.emf = Persistence.createEntityManagerFactory("CasaCulturaPU");
        this.em = emf.createEntityManager();
    }

    public String getTextoBuscar() {
        return textoBuscar;
    }

    public void setTextoBuscar(String textoBuscar) {
        this.textoBuscar = textoBuscar;
    }       
        
    public Integer getMaximoResultados() {
        return maximoResultados;
    }

    public void setMaximoResultados(Integer maximResultats) {
        this.maximoResultados = maximResultats;
    }
    
    @Override
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
