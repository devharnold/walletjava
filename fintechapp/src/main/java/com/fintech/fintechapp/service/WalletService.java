package com.fintech.fintechapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.fintechapp.model.Wallet;
import com.fintech.fintechapp.model.Transaction;
import com.fintech.fintechapp.repository.WalletRepository;
import com.fintech.fintechapp.repository.TransactionRepository;

@Service // Annotation:: Wallet service, handles the business logic
public class WalletService {
    
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Autowired // Automatically injects the WalletRepository and TransactionRepository
    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Optional<Double> getWalletBalance(Long id) {
        return walletRepository.findWalletBalance(id);
    }
    
    public Optional<Wallet> getWalletByWalletId(String walletId) {
        return walletRepository.findByWalletId(walletId);
    }

    @Transactional
//    public Transaction transfer(String senderWalletId, String receiverWalletId, BigDecimal amount, String message) {
//        Wallet sender = walletRepository.findByWalletId(senderWalletId)
//            .orElseThrow(() -> new RuntimeException("Sender wallet not found!"));
//
//        Wallet receiver = walletRepository.findByWalletId(receiverWalletId)
//            .orElseThrow(() -> new RuntimeException("Receiver wallet not found!"));
//
//        if (sender.getBalance().compareTo(amount) < 0) {
//            throw new RuntimeException("Insufficient balance!");
//        }
//
//        // Debit sender
//        sender.setBalance(sender.getBalance().subtract(amount));
//
//        // Credit receiver
//        receiver.setBalance(receiver.getBalance().add(amount));
//
//        // Save updated balances
//        walletRepository.save(sender);
//        walletRepository.save(receiver);

        // Create and save transaction
//        Transaction transaction = new Transaction();
//        transaction.setTransactionRef(UUID.randomUUID().toString());
//        transaction.setSender(sender);
//        transaction.setReceiver(receiver);
//        transaction.setAmount(amount);
//        transaction.setTimestamp(LocalDateTime.now());
//        transaction.setMessage(message);

        // Save transaction and return
        //return transactionRepository.save(transaction);
    }
}

//public class WalletService {
//
//    private final WalletDAO walletDAO;
//    private final Connection connection;
//
//    public WalletService(Connection connection) {
//        this.connection = connection;
//        this.walletDAO = new WalletDAO(connection);
//    }
//
//    public Optional<Wallet> getWalletByUserId(Long userId) {
//        try {
//            return walletDAO.getWalletByUserId(userId);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }
//
//    public void transfer(int fromWalletId, int toWalletId, BigDecimal amount) throws Exception {
//        try {
//            connection.setAutoCommit(false);
//
//            Wallet fromWallet = walletDAO.getWalletById(fromWalletId)
//                    .orElseThrow(() -> new Exception("Sender wallet not found"));
//            Wallet toWallet = walletDAO.getWalletById(toWalletId)
//                    .orElseThrow(() -> new Exception("Receiver wallet not found"));
//
//            if (fromWallet.getBalance().compareTo(amount) < 0) {
//                throw new Exception("Insufficient balance");
//            }
//
//            fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
//            toWallet.setBalance(toWallet.getBalance().add(amount));
//
//            walletDAO.updateWalletBalance(fromWallet);
//            walletDAO.updateWalletBalance(toWallet);
//
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), fromWalletId, "TRANSFER", amount.negate(), "P2P transfer", "SUCCESS"
//            ));
//
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), toWalletId, "TRANSFER", amount, "P2P transfer", "SUCCESS"
//            ));
//
//            connection.commit();
//        } catch (Exception e) {
//            connection.rollback();
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), fromWalletId, "TRANSFER", amount, "P2P transfer failed", "ROLLED_BACK"
//            ));
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    public void withdraw(int walletId, BigDecimal amount, String description) throws Exception {
//        try {
//            connection.setAutoCommit(false);
//
//            Wallet wallet = walletDAO.getWalletById(walletId)
//                    .orElseThrow(() -> new Exception("Wallet not found"));
//
//            if (wallet.getBalance().compareTo(amount) < 0) {
//                throw new Exception("Insufficient funds");
//            }
//
//            wallet.setBalance(wallet.getBalance().subtract(amount));
//            walletDAO.updateWalletBalance(wallet);
//
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), walletId, "WITHDRAWAL", amount.negate(), description, "SUCCESS"
//            ));
//
//            connection.commit();
//        } catch (Exception e) {
//            connection.rollback();
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), walletId, "WITHDRAWAL", amount, description + " failed", "ROLLED_BACK"
//            ));
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    public void debitSubscription(int walletId, BigDecimal amount, String description) throws Exception {
//        try {
//            connection.setAutoCommit(false);
//
//            Wallet wallet = walletDAO.getWalletById(walletId)
//                    .orElseThrow(() -> new Exception("Wallet not found"));
//
//            if (wallet.getBalance().compareTo(amount) < 0) {
//                throw new Exception("Insufficient funds for subscription");
//            }
//
//            wallet.setBalance(wallet.getBalance().subtract(amount));
//            walletDAO.updateWalletBalance(wallet);
//
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), walletId, "SUBSCRIPTION", amount.negate(), description, "SUCCESS"
//            ));
//
//            connection.commit();
//        } catch (Exception e) {
//            connection.rollback();
//            walletDAO.logTransaction(new TransactionLog(
//                    generateRef(), walletId, "SUBSCRIPTION", amount, description + " failed", "ROLLED_BACK"
//            ));
//            throw e;
//        } finally {
//            connection.setAutoCommit(true);
//        }
//    }
//
//    private String generateRef() {
//        return "TXN-" + System.currentTimeMillis();
//    }
//}
