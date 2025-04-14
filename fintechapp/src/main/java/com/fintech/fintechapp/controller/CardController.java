package com.fintech.fintechapp.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.fintech.fintechapp.model.Card;
import com.fintech.fintechapp.service.CardService;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card createdCard = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @GetMapping("/api/cards/{cardNumber}")
    public ResponseEntity<Card> getCardNumber(@PathVariable Integer cardNumber) {
        Card card = cardService.getCardByNumber(cardNumber);
    }

    @GetMapping("/api/cards/{cardSubscriptions}")
    public ResponseEntity<Card> getManagedSubscriptions(@PathVariable String subscriptions) {
        Card card = cardService.getSubscriptions(subscriptions);
    }
}