package com.company.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "buyers")
@Data
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyer_id")
    private int id;

    @Column(name = "last_name")
    private String lastName;

    private String district;
    private int discount;
}
