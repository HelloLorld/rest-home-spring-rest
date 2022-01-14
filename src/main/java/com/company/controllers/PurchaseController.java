package com.company.controllers;

import com.company.entities.Purchase;
import com.company.entities.Shop;
import com.company.exception.ResourceNotFound;
import com.company.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

@RestController
public class PurchaseController {
    @Autowired
    private PurchaseRepository repository;

    @GetMapping("/purchases")
    public List<Purchase> getAllPurchases() {
        return repository.findAll();
    }
    @GetMapping("/purchases/{id}")
    public ResponseEntity<?> getPurchase(@PathVariable(value = "id") Integer id) {
        Optional<Purchase> shop = repository.findById(id);
        if (!shop.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(shop.get(), HttpStatus.OK);
    }

    @GetMapping("/months_of_purchases")
    public Set<Month> getAllMonths() {
        List<Purchase> purchases = getAllPurchases();
        Set<Month> setOfMonths = new HashSet<>();
        for (Purchase purchase : purchases) {
            LocalDate localDate = purchase.getDateOfPurchase().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            setOfMonths.add(Month.of(month));
            if (setOfMonths.size() == 12) break;
        }
        return setOfMonths;
    }

    @GetMapping("/amount")
    public List<Purchase> getAmounts() {
        List<Purchase> purchases = getAllPurchases();
        purchases.removeIf(purchase -> purchase.getAmount() * purchase.getQuantity() < 60000);
        return purchases;
    }
    @GetMapping("/districts_purchases")
    public List<Purchase> getDistrictsPurchases() {
        List<Purchase> purchases = repository.getPurchaseByDistrictBuyer();
        purchases.sort(((o1, o2) -> o1.compareTo(o2)));
        return purchases;
    }

    @GetMapping("/discounts_of_buyers")
    public Set<Shop> getShopsWithDiscountOfBuyers() {
        List<Purchase> purchases = getAllPurchases();
        purchases.removeIf(purchase -> purchase.getBuyer().getDiscount()<10 || purchase.getBuyer().getDiscount()>15);
        Set<Shop> shops = new HashSet<>();
        for (Purchase purchase : purchases)
            shops.add(purchase.getSeller());
        return shops;
    }

    @PostMapping("/purchases")
    public Purchase savePurchase(@RequestBody Purchase purchase) {
        return repository.save(purchase);
    }

    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable(value = "id") Integer id) {
        Optional<Purchase> shop = repository.findById(id);
        if (!shop.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else repository.delete(shop.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/purchases/{id}")
    public ResponseEntity<?> putOfPurchase(@PathVariable(value = "id") Integer id, @RequestBody Purchase purchaseChanged) {
        Optional<Purchase> purchase = repository.findById(id);
        if (!purchase.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            purchaseChanged.setId(purchase.get().getId());
            repository.save(purchaseChanged);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("changed", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/purchases/{id}")
    public ResponseEntity<?> patchOfPurchase(@PathVariable(value = "id") Integer id, @RequestBody Purchase purchaseChanged) {
        Optional<Purchase> purchase = repository.findById(id);
        boolean needChange = false;
        if (!purchase.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            purchaseChanged.setId(purchase.get().getId());
            if (!purchase.get().getDateOfPurchase().equals(purchaseChanged.getDateOfPurchase())) {
                needChange = true;
            }
            if (!purchase.get().getBook().equals(purchaseChanged.getBook())) {
                needChange = true;
            }
            if (!purchase.get().getBuyer().equals(purchaseChanged.getBuyer())) {
                needChange = true;
            }
            if(!purchase.get().getSeller().equals(purchaseChanged.getSeller())) {
                needChange = true;
            }
            if(purchase.get().getAmount() != purchaseChanged.getAmount()) {
                needChange = true;
            }
            if(purchase.get().getQuantity() != purchaseChanged.getQuantity()) {
                needChange = true;
            }
            if (needChange) repository.save(purchaseChanged);
        }

        Map<String, Boolean> response = new HashMap<>();
        if (needChange) {
            response.put("changed", true);
        } else {
            response.put("changed", false);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
