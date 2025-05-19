/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author juanje
 */
@Entity
@Table(name = "ratings")
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
    @NamedQuery(name = "Rating.findByUserId", query = "SELECT r FROM Rating r WHERE r.ratingPK.userId = :userId"),
    @NamedQuery(name = "Rating.findByCopyId", query = "SELECT r FROM Rating r WHERE r.ratingPK.copyId = :copyId"),
    @NamedQuery(name = "Rating.findByRating", query = "SELECT r FROM Rating r WHERE r.rating = :rating")})
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RatingPK ratingPK;
    @Basic(optional = false)
    @Column(name = "rating")
    private int rating;
    @JoinColumn(name = "copy_id", referencedColumnName = "copy_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Copy copy;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Rating() {
    }

    public Rating(RatingPK ratingPK) {
        this.ratingPK = ratingPK;
    }

    public Rating(RatingPK ratingPK, int rating) {
        this.ratingPK = ratingPK;
        this.rating = rating;
    }

    public Rating(int userId, int copyId) {
        this.ratingPK = new RatingPK(userId, copyId);
    }

    public RatingPK getRatingPK() {
        return ratingPK;
    }

    public void setRatingPK(RatingPK ratingPK) {
        this.ratingPK = ratingPK;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ratingPK != null ? ratingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.ratingPK == null && other.ratingPK != null) || (this.ratingPK != null && !this.ratingPK.equals(other.ratingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wearefive.casacultura.entities.Rating[ ratingPK=" + ratingPK + " ]";
    }
    
}
