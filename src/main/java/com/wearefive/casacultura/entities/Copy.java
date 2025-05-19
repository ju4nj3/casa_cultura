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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author juanje
 */
@Entity
@Table(name = "copies")
@NamedQueries({
    @NamedQuery(name = "Copy.findAll", query = "SELECT c FROM Copy c"),
    @NamedQuery(name = "Copy.findByCopyId", query = "SELECT c FROM Copy c WHERE c.copyId = :copyId")})
public class Copy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "copy_id")
    private Integer copyId;
    @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    @ManyToOne(optional = false)
    private Book bookId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "copyId")
    private List<Rental> rentalList;

    public Copy() {
    }

    public Copy(Integer copyId) {
        this.copyId = copyId;
    }

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
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
        hash += (copyId != null ? copyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Copy)) {
            return false;
        }
        Copy other = (Copy) object;
        if ((this.copyId == null && other.copyId != null) || (this.copyId != null && !this.copyId.equals(other.copyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wearefive.casacultura.entities.Copy[ copyId=" + copyId + " ]";
    }
    
}
