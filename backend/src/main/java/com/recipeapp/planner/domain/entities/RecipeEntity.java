package com.recipeapp.planner.domain.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID recipeId;

    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(name = "recipe_instruction_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "step")
    private List<String> instructions;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by", referencedColumnName = "userId")
    private UserEntity createdBy;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

    @JsonGetter("createdByUserId")
    public UUID getCreatedByUserId() {
        return createdBy != null
                ? createdBy.getUserId()
                : null;
    }

    public void addIngredientLink(RecipeIngredientEntity link) {
        recipeIngredients.add(link);
        link.setRecipe(this);
    }

    public void removeIngredientLink(RecipeIngredientEntity link) {
        recipeIngredients.remove(link);
        link.setRecipe(null);
    }
}
