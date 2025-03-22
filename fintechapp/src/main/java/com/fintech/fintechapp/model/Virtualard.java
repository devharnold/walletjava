/**
 * Virtual card class
 * Simulates the traditional card but this is meant for online payments
 * Expiry date should be set after 1 year per every card.
 * Every card will/must have a unique card number
 */

package com.fintech.fintechapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name="virtualcards")
public class Virtualard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private Long userId; // User that holds the specific card

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance; // the total funds available in that card
}