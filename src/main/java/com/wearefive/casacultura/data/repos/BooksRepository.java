/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.data.repos;

import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;

/**
 *
 * @author juanje
 */
public class BooksRepository extends BaseRepository {
        
    private TypedQuery query;
        
    public String getPredicadoWHERE() {
    
        String predicadoWHERE = "";

        if (super.getTextoBuscar() == null || super.getTextoBuscar().trim().isEmpty()) {
            predicadoWHERE+=(predicadoWHERE.isEmpty() ? "WHERE " : "AND") + " (b.title LIKE :text OR b.original_title LIKE :text)";
        }

        predicadoWHERE += " ORDER BY b.originalPublicationYear DESC";
        
        return predicadoWHERE;
    }
    
    private void setParametros() {
        
        if (super.getTextoBuscar() == null || super.getTextoBuscar().trim().isEmpty()) {   
            query.setParameter("text", "%" + super.getTextoBuscar()+"%");
        }        
    }
    
    public List<Book> getAll() {
        
        try {
            
            return em.createNamedQuery("Book.findAll", Book.class).getResultList();    
        }       
        catch(Exception ex) {
            return null;
        }        
    }

    
    public List<Book> getLista() {
        
        try {
            
            query =  em.createQuery("SELECT b FROM Book b " + this.getPredicadoWHERE(), Book.class);
            setParametros();
            
            if(this.getMaximoResultados()!=null)
                query.setMaxResults(this.getMaximoResultados());

            return query.getResultList();

        }       
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }   
    }
    
    public Book getId(Integer id) {
        
        try {
            return em.createNamedQuery("Books.findByBookId", Book.class).setParameter("bookId", id).getSingleResult();        
        }       
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }        
    }    
    
    public Long getTotal() {
        
        try {
                        
            return em.createQuery("SELECT COUNT(b) FROM Book b", Long.class).getSingleResult();
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }
    
    public List<Book> getLibrosRecomendados(Set<Integer> idsRecomendados) {
         
        try {

            return em.createQuery("SELECT b FROM Book b WHERE b.bookId IN :ids", Book.class)
                .setParameter("ids", idsRecomendados)
                .getResultList();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
    
    public List<Book> getLibrosPorGeneros(List<String> generos) {

        try {
            return em.createQuery("SELECT DISTINCT b FROM Book b JOIN b.genreList g WHERE LOWER(g.name) IN :generos", Book.class)
                .setParameter("generos", generos)
                .getResultList();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Book> getLibrosPorComentarios(User usuario) {
        
        try {
            
            String comentario = usuario.getComentario();

            if (comentario == null || comentario.trim().isEmpty()) {
                return new ArrayList<>();
            }

            // Extraer palabras clave del comentario
            
            List<String> palabrasClave = Arrays.stream(comentario.toLowerCase().split("\\W+"))
                .filter(p -> p.length() > 3) // quitamos palabras cortas
                .collect(Collectors.toList());

            if (palabrasClave.isEmpty()) return new ArrayList<>();
            
            String query = "SELECT DISTINCT b FROM Book b WHERE " +
                palabrasClave.stream()
                    .map(p -> "LOWER(b.title) LIKE '%" + p + "%'")
                    .collect(Collectors.joining(" OR "));

            return em.createQuery(query, Book.class).getResultList();
            
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Book> getLibrosPorTitulos(List<String> palabras) {
        
        try {
            
            String query = "SELECT DISTINCT b FROM Book b WHERE " + palabras.stream().map(p -> "LOWER(b.title) LIKE '%" + p + "%'")
            .collect(Collectors.joining(" OR "));

            return em.createQuery(query, Book.class).getResultList();    
            
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
}
