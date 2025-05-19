/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author juanje
 */
@Embeddable
public class RatingPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "copy_id")
    private int copyId;

    public RatingPK() {
    }

    public RatingPK(int userId, int copyId) {
        this.userId = userId;
        this.copyId = copyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) copyId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RatingPK)) {
            return false;
        }
        RatingPK other = (RatingPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.copyId != other.copyId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wearefive.casacultura.entities.RatingPK[ userId=" + userId + ", copyId=" + copyId + " ]";
    }
    
}
