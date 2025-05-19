/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.data.repos;

import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Rental;
import com.wearefive.casacultura.entities.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author juanje
 */
public class RentalsRepository extends BaseRepository {
                  
    public List<Rental> getLista() {
        
        try {
            
            return em.createNamedQuery("Rental.findAllOrderByDesc", Rental.class).getResultList();

        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }
    
    public List<Rental> getListaPorIdUsuario(User user) {
        
        try {
            
            return em.createNamedQuery("Rental.findByUserId", Rental.class).setParameter("userId", user).getResultList();

        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }
    
    public List<Set<Integer>> getTransacciones() {
        
        try {
            
            List<User> usuarios = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            
             return usuarios.stream().map(u -> u.getRentalList().stream().map(r -> r.getCopyId().getBookId().getBookId()).collect(Collectors.toSet()))
            .filter(s -> s.size() > 1)
            .collect(Collectors.toList());

        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }
    
    public Integer getAlquileresActivos() {
        try {
            return em.createQuery("SELECT COUNT(r) FROM Rental R WHERE r.returnDate IS NULL").getFirstResult();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
    
    public Book getId(Integer id) {
        
        try {
            return em.createNamedQuery("Rental.findByRentalId", Book.class).setParameter("rentalId", id).getSingleResult();        
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
    
    public void Guardar(Rental rental) {
            
        EntityTransaction tx = em.getTransaction();
        
        try {
            
            tx.begin();
            em.merge(rental);
            tx.commit();
            
        } catch (Exception e) {
            
            if (tx.isActive())
                tx.rollback();
            
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el alquiler", e);
            
        } finally {        
            em.close();
        }                
    }    
    
    public Long getTotal() {
        
        try {
                        
            return em.createQuery("SELECT COUNT(r) FROM Rental r", Long.class).getSingleResult();
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }   
    
    public List<Object[]> getLibrosMasPrestados(int maxResultados) {
        
        try {
        return em.createQuery("SELECT cp.bookId.title, COUNT(r) FROM Rental r JOIN r.copyId cp GROUP BY cp.bookId.title ORDER BY COUNT(r) DESC", Object[].class)
            .setMaxResults(maxResultados)
            .getResultList();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }  
    }
    
    public Map<String, Long> getEstadisticasDevoluciones() {
        
        try {
            
            Long noDevueltos = em.createQuery("SELECT COUNT(r) FROM Rental r WHERE r.returnDate IS NULL", Long.class).getSingleResult();
            Long devueltos = em.createQuery("SELECT COUNT(r) FROM Rental r WHERE r.returnDate IS NOT NULL", Long.class).getSingleResult();

            Map<String, Long> mapa = new HashMap<>();
            mapa.put("Pendientes", noDevueltos);
            mapa.put("Devueltos", devueltos);
            return mapa;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
        
    }
}
