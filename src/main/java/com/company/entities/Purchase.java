package com.company.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Purchase implements Comparable<Purchase>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date_of_purchase")
    private Date dateOfPurchase;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller", referencedColumnName = "shop_id")
    private Shop seller;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer", referencedColumnName = "buyer_id")
    private Buyer buyer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book", referencedColumnName = "book_id")
    private Book book;
    private int quantity;
    private int amount;

    @Override
    public int compareTo(Purchase o) {
        if (this.amount*this.quantity > o.getAmount()*o.getQuantity()) return 1;
        else if (this.amount*this.quantity < o.getAmount()*o.getQuantity())return -1;
        else return 0;
    }
}
