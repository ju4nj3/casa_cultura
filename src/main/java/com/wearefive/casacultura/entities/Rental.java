/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author juanje
 */
@Entity
@Table(name = "rentals")
@NamedQueries({
    @NamedQuery(name = "Rental.findAll", query = "SELECT r FROM Rental r"),
    @NamedQuery(name = "Rental.findAllOrderByDesc", query = "SELECT r FROM Rental r ORDER BY r.date DESC"),
    @NamedQuery(name = "Rental.findByRentalId", query = "SELECT r FROM Rental r WHERE r.rentalId = :rentalId"),
    @NamedQuery(name = "Rental.findByUserId", query = "SELECT r FROM Rental r WHERE r.userId = :userId ORDER BY r.date DESC"),
    @NamedQuery(name = "Rental.findByDate", query = "SELECT r FROM Rental r WHERE r.date = :date"),
    @NamedQuery(name = "Rental.findByDue", query = "SELECT r FROM Rental r WHERE r.due = :due"),
    @NamedQuery(name = "Rental.findByReturnDate", query = "SELECT r FROM Rental r WHERE r.returnDate = :returnDate")})
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "due")
    @Temporal(TemporalType.TIMESTAMP)
    private Date due;
    @Column(name = "return")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    @JoinColumn(name = "copy_id", referencedColumnName = "copy_id")
    @ManyToOne(optional = false)
    private Copy copyId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User userId;

    public Rental() {
    }

    public Rental(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public Rental(Integer rentalId, Date date, Date due) {
        this.rentalId = rentalId;
        this.date = date;
        this.due = due;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Copy getCopyId() {
        return copyId;
    }

    public void setCopyId(Copy copyId) {
        this.copyId = copyId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalId != null ? rentalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rental)) {
            return false;
        }
        Rental other = (Rental) object;
        if ((this.rentalId == null && other.rentalId != null) || (this.rentalId != null && !this.rentalId.equals(other.rentalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wearefive.casacultura.entities.Rental[ rentalId=" + rentalId + " ]";
    }
    
}
