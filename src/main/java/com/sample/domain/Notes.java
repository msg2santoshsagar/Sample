package com.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Notes.
 */
@Entity
@Table(name = "notes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "notes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NoteCategory> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Notes title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public Notes date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public Notes place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Notes phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<NoteCategory> getCategories() {
        return categories;
    }

    public Notes categories(Set<NoteCategory> noteCategories) {
        this.categories = noteCategories;
        return this;
    }

    public Notes addCategory(NoteCategory noteCategory) {
        categories.add(noteCategory);
        noteCategory.setNotes(this);
        return this;
    }

    public Notes removeCategory(NoteCategory noteCategory) {
        categories.remove(noteCategory);
        noteCategory.setNotes(null);
        return this;
    }

    public void setCategories(Set<NoteCategory> noteCategories) {
        this.categories = noteCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notes notes = (Notes) o;
        if(notes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notes{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", date='" + date + "'" +
            ", place='" + place + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            '}';
    }
}
