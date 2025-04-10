package com.fintech.fintechapp.service;

import java.util.*;
import com.fintech.fintechapp.model.VirtualCard;
import org.springframework.stereotype.Service;
import com.fintech.fintechapp.repository.VirtualCardRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service // Annotation:: VirtualCardService handles the virtualcards business logic
public class VirtualCardService {
    private final VirtualCardRepository virtualCardRepository;

    @Autowired // Automatically injects the VirtualCardRepository
    public VirtualCardService(VirtualCardRepository virtualCardRepository) {
        this.virtualCardRepository = virtualCardRepository;
    }

    public List<VirtualCard> getAllVirtualCards() {
        return virtualCardRepository.findAll();
    }

    public VirtualCard getVirtualCardById(Long id) {
        return virtualCardRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Wallet not found!"));
    }
}