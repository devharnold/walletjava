package com.fintech.fintechapp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.fintechapp.model.VirtualCard;
import com.fintech.fintechapp.repository.VirtualCardRepository;

@RestController
public class VirtualCardController {
    private final VirtualCardRepository repository;

    VirtualCardController(VirtualCardRepository respository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/virtualcards")
    List<VirtualCard> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    // create a new card
    @PostMapping("/virtualcards")
    VirtualCard newVirtualCard(@RequestBody VirtualCard newVirtualCard) {
        return repository.save(newVirtualCard);
    }

    // Single item
    @GetMapping("/virtualcards/{id}")
    VirtualCard one(@PathVariable Long id) {
        return repository.findById(id)
        .orElseThrow(() -> newVirtualCardNotFoundException(id));
    }

    @PutMapping("/virtualcards/{id}")
    VirtualCard replaceVirtualCard(@RequestBody VirtualCard newVirtualCard, @PathVariable Long id) {
        return repository.findById(id)
        .map(virtualcard -> {
            virtualcard.setUserId(newVirtualCard.getUserId());
            virtualcard.setVirtualCardNumber(newVirtualCard.getVirtualCardNumber());
            return repository.save(virtualcard);
        })
        .orElseGet(() -> {
            return repository.save(newVirtualCard);
        })
    }
}