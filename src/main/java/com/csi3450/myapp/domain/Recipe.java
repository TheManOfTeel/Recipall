package com.csi3450.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "recipe")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "time")
    private Integer time;

    @Column(name = "difficulty")
    private Integer difficulty;

    @Column(name = "rating")
    private Double rating;

    @NotNull
    @Column(name = "steps", nullable = false)
    private String steps;

    @Column(name = "creation_date")
    private Instant creationDate;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score")
    private Integer score;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("recipes")
    private User user;

    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IngredientList> ingredientLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public Recipe time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Recipe difficulty(Integer difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Double getRating() {
        return rating;
    }

    public Recipe rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getSteps() {
        return steps;
    }

    public Recipe steps(String steps) {
        this.steps = steps;
        return this;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Recipe creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public Recipe description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Recipe name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public Recipe score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public Recipe user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<IngredientList> getIngredientLists() {
        return ingredientLists;
    }

    public Recipe ingredientLists(Set<IngredientList> ingredientLists) {
        this.ingredientLists = ingredientLists;
        return this;
    }

    public Recipe addIngredientList(IngredientList ingredientList) {
        this.ingredientLists.add(ingredientList);
        ingredientList.setRecipe(this);
        return this;
    }

    public Recipe removeIngredientList(IngredientList ingredientList) {
        this.ingredientLists.remove(ingredientList);
        ingredientList.setRecipe(null);
        return this;
    }

    public void setIngredientLists(Set<IngredientList> ingredientLists) {
        this.ingredientLists = ingredientLists;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return id != null && id.equals(((Recipe) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", time=" + getTime() +
            ", difficulty=" + getDifficulty() +
            ", rating=" + getRating() +
            ", steps='" + getSteps() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            ", score=" + getScore() +
            "}";
    }
}
