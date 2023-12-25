package com.bezkoder.spring.jpa.postgresql.model;

//import jakarta.persistence.*;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table( name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column( name = "user_id")
    private long userId;

    @Column(name = "balance")
    private BigDecimal balance;

    public Account() {

    }

    public Account(long userId,BigDecimal balance) {
        this.userId= userId;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account [userId=" + userId + ", balance=" + balance+"]";
    }

}