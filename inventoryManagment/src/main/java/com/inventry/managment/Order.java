package com.inventry.managment;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class Order {
    @Id

    private int oid;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private product product;

    private int quantity;
    private Date orderDate;

    // Getters and Setters
}

