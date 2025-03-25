package com.fintech.fintechapp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.repository.TransactionRepository;



@RestController
public class TransactionController {
    private final TransactionRepository repository;

    TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    //Aggregate-root
    @GetMapping("/transaction")
    List<Transaction> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    // create a new wallet
    @PostMapping("/transaction")
    Transaction newTransaction(@RequestBody Transaction newTransaction) {
        return repository.save(newTransaction);
    }

    // single item::get transaction by the transactionid
    @GetMapping("/transactions/{id}")
    Transaction one(@PathVariable Long id) {
        return repository.findbyId()
            .orElseThrow(() -> new TransactionNotFoundException(id));
    }
}
