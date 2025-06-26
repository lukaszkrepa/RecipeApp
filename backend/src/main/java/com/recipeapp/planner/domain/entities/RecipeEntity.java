package com.recipeapp.planner.domain.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @JsonGetter("createdByUserId")
    public UUID getCreatedByUserId() {
        return createdBy != null
                ? createdBy.getUserId()
                : null;
    }
}
