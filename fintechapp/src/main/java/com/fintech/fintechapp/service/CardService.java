package com.fintech.fintechapp.service;

import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.model.Card;
import com.fintech.fintechapp.repository.WalletRepository;
import com.fintech.fintechapp.repository.TransactionRepository;
import com.fintech.fintechapp.repository.CardRepository;

@Service
public class CardService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    public CardService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    // method to find a card's balance
    public Optional<Double> getCardBalance(Integer cardNumber) {
        return cardRepository.findCardBalance(cardNumber);
    }

    public Card createCard(Card card) {
        return cardRepository.createVirtualcard(card);
    }

}