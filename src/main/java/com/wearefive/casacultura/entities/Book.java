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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author juanje
 */
@Entity
@Table(name = "books")
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b ORDER BY b.originalPublicationYear"),
    @NamedQuery(name = "Book.findAllOrderedDescByOriginalPublicationYear", query = "SELECT b FROM Book b ORDER BY b.originalPublicationYear DESC"),
    @NamedQuery(name = "Book.findByIsbn", query = "SELECT b FROM Book b WHERE b.isbn = :isbn"),
    @NamedQuery(name = "Book.findByAuthors", query = "SELECT b FROM Book b WHERE b.authors = :authors"),
    @NamedQuery(name = "Book.findByOriginalPublicationYear", query = "SELECT b FROM Book b WHERE b.originalPublicationYear = :originalPublicationYear"),
    @NamedQuery(name = "Book.findByOriginalTitle", query = "SELECT b FROM Book b WHERE b.originalTitle = :originalTitle"),
    @NamedQuery(name = "Book.findByTitle", query = "SELECT b FROM Book b WHERE b.title = :title"),
    @NamedQuery(name = "Book.findByLanguageCode", query = "SELECT b FROM Book b WHERE b.languageCode = :languageCode"),
    @NamedQuery(name = "Book.findByImageUrl", query = "SELECT b FROM Book b WHERE b.imageUrl = :imageUrl"),
    @NamedQuery(name = "Book.findByBookId", query = "SELECT b FROM Book b WHERE b.bookId = :bookId")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "authors")
    private String authors;
    @Column(name = "original_publication_year")
    private Integer originalPublicationYear;
    @Column(name = "original_title")
    private String originalTitle;
    @Column(name = "title")
    private String title;
    @Column(name = "language_code")
    private String languageCode;
    @Column(name = "image_url")
    private String imageUrl;
    @Id
    @Basic(optional = false)
    @Column(name = "book_id")
    private Integer bookId;
    @JoinTable(name = "books_genres", joinColumns = {
        @JoinColumn(name = "book_id", referencedColumnName = "book_id")}, inverseJoinColumns = {
        @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")})
    @ManyToMany
    private List<Genre> genreList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId")
    private List<Copy> copyList;

    public Book() {
    }

    public Book(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Integer getOriginalPublicationYear() {
        return originalPublicationYear;
    }

    public void setOriginalPublicationYear(Integer originalPublicationYear) {
        this.originalPublicationYear = originalPublicationYear;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<Copy> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<Copy> copyList) {
        this.copyList = copyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wearefive.casacultura.entities.Book[ bookId=" + bookId + " ]";
    }
    
}
