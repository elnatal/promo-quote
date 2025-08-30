package com.elnatal.promo_quoter.repository;

import com.elnatal.promo_quoter.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Query("SELECT p FROM Promotion p WHERE p.active = true ORDER BY p.priorityOrder ASC")
    List<Promotion> findActivePromotionsOrderedByPriority();
}
