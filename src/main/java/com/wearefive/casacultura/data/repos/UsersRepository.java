/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.data.repos;

import com.wearefive.casacultura.entities.User;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juanje
 */
public class UsersRepository extends BaseRepository {
      
    public List<User> getAll() {
        
        try {
            
            return em.createNamedQuery("User.findAll", User.class).getResultList();    
        }       
        catch(Exception ex) {
            return null;
        }        
    }
    
    public User getId(Integer id) {
        
        try {
            return em.createNamedQuery("User.findByUserId", User.class).setParameter("userId", id).getSingleResult();        
        }       
        catch(Exception ex) {
            return null;
        }        
    }
    
    public Map<String, Long> getEstadisticasGenero() {
    
        try {
            
            Map<String, Long> m = new HashMap<>();

            List<Object[]> resultados = em.createQuery("SELECT u.sexo, COUNT(u) FROM User u GROUP BY u.sexo", Object[].class).getResultList();

            for (Object[] fila : resultados) {
                m.put((String) fila[0], (Long) fila[1]);
            }

            return m;
        }
        catch(Exception ex) {
            return null;
        }   
    }
    
    public Map<String, Long> getEstadisticasPorRangoEdad() {
        
        try {
            
            List<User> usuarios = em.createQuery("SELECT u FROM User u", User.class).getResultList();

            Map<String, Long> contadores = new HashMap<>();

            LocalDate hoy = LocalDate.now();

            for (User u : usuarios) {
                
                if (u.getFechaNacimiento()== null) continue;

                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                LocalDate nacimiento = LocalDate.parse(u.getFechaNacimiento(), formato);
                int edad = Period.between(nacimiento, hoy).getYears();
                
                String rango;
                if (edad <= 17) {
                    rango = "Menores";
                } else if (edad <= 30) {
                    rango = "Jóvenes";
                } else if (edad <= 50) {
                    rango = "Adultos";
                } else if (edad <= 65) {
                    rango = "Mayores";
                } else {
                    rango = "Tercera edad";
                }

                contadores.put(rango, contadores.getOrDefault(rango, 0L) + 1);
            }

            return contadores;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
}
    
    public Long getTotal() {
        
        try {
                        
            return em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }   
    }    
}
