package com.fintech.fintechapp.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionLog {

    private int id;
    private String transactionRef;
    private int walletId;
    private String type; // PAYMENT, TRANSFER, WITHDRAWAL, SUBSCRIPTION
    private BigDecimal amount;
    private String description;
    private String status; // PENDING, SUCCESS, FAILED, ROLLED_BACK
    private LocalDateTime timestamp;

    public TransactionLog() {}

    // Constructor
    public TransactionLog(String transactionRef, int walletId, String type, BigDecimal amount, String description, String status) {
        this.transactionRef = transactionRef;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public int getWalletId() { return walletId; }
    public void setWalletId(int walletId) { this.walletId = walletId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
