/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.data.repos;

import com.wearefive.casacultura.data.models.BookRating;
import java.util.List;

/**
 *
 * @author juanje
 */
public class RatingsRepository extends BaseRepository {
                  
    public BookRating getValores(Integer bookId) {
        
        try {
                        
            Object[] resultado = em.createQuery("SELECT COUNT(r), AVG(r.rating) FROM Rating r where r.copy.bookId.bookId = :bookId", Object[].class)
                    .setParameter("bookId", bookId).getSingleResult();
            
            BookRating br = new BookRating();
            br.setTotal((Long)resultado[0]);
            br.setPromedio((Double)resultado[1]);
            
            return br;
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }  
    
    public Long getTotal() {
        
        try {
                        
            return em.createQuery("SELECT COUNT(r) FROM Rating r", Long.class).getSingleResult();
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }  
    
    public List<Object[]> getLibrosMasValorados(int maxResultados) {
        try {
            return em.createQuery("SELECT r.copy.bookId.title, AVG(r.rating) FROM Rating r GROUP BY r.copy.bookId.title ORDER BY AVG(r.rating) DESC", Object[].class)
            .setMaxResults(maxResultados)
            .getResultList();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }  
    }
}
