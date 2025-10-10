package com.ar2030.finbank.cards.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cards extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "mobile_number", nullable = false, length = 10)
    private String mobileNumber;

    @Column(name = "card_number", nullable = false, length = 100)
    private String cardNumber;

    @Column(name = "card_type", nullable = false, length = 100)
    private String cardType;

    @Column(name = "total_limit", nullable = false)
    private Integer totalLimit;

    @Column(name = "amount_used", nullable = false)
    private Integer amountUsed;

    @Column(name = "available_amount", nullable = false)
    private Integer availableAmount;

}
