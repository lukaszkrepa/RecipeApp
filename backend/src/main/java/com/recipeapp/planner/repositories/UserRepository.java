package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findUserEntitieByUsername(String username);

    boolean existsByUsername(String user);

}
