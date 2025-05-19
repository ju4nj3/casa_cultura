/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author juanje
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.userId"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findBySexo", query = "SELECT u FROM User u WHERE u.sexo = :sexo"),
    @NamedQuery(name = "User.findByComentario", query = "SELECT u FROM User u WHERE u.comentario = :comentario"),
    @NamedQuery(name = "User.findByFechaNacimiento", query = "SELECT u FROM User u WHERE u.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "User.findByNombre", query = "SELECT u FROM User u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "User.findByApellido", query = "SELECT u FROM User u WHERE u.apellido = :apellido"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByDniNie", query = "SELECT u FROM User u WHERE u.dniNie = :dniNie"),
    @NamedQuery(name = "User.findByTelefono", query = "SELECT u FROM User u WHERE u.telefono = :telefono"),
    @NamedQuery(name = "User.findByAdmin", query = "SELECT u FROM User u WHERE u.admin = :admin")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "sexo")
    private String sexo;
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "dni_nie")
    private String dniNie;
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "admin")
    private boolean admin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Rating> ratingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<Rental> rentalList;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String fechaNacimiento, String nombre, String apellido, String dniNie, boolean admin) {
        this.userId = userId;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dniNie = dniNie;
        this.admin = admin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDniNie() {
        return dniNie;
    }

    public void setDniNie(String dniNie) {
        this.dniNie = dniNie;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public List<Rental> getRentalList() {
        return rentalList;
    }

    public void setRentalList(List<Rental> rentalList) {
        this.rentalList = rentalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wearefive.casacultura.entities.User[ userId=" + userId + " ]";
    }
    
    public String getNombreApellido() {
        return nombre + " " + apellido;
    }
    
}
