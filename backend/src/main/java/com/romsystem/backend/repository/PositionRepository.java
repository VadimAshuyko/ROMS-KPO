package com.romsystem.backend.repository;

import com.romsystem.backend.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByDishId(Long dishId);
}
