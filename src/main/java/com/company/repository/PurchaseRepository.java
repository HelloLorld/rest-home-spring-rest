package com.company.repository;

import com.company.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
    @Query(value = "select purchases.id, purchases.date_of_purchase, purchases.seller, purchases.buyer, purchases.book," +
            " purchases.quantity, purchases.amount from purchases inner join buyers AS b ON b.buyer_id = purchases.buyer" +
            " inner join shops AS s on s.shop_id = purchases.seller  WHERE b.district = s.district", nativeQuery = true)
    List<Purchase> getPurchaseByDistrictBuyer();
}
