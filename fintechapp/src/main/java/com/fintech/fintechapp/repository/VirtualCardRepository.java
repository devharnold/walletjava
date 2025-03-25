package com.fintech.fintechapp.repository;

import com.fintech.fintechapp.model.VirtualCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VirtualCardRepository extends JpaRepository<VirtualCard, Long> {
    Optional<VirtualCard> findByVirtualCardNumber(String virtualCardNumber);
}