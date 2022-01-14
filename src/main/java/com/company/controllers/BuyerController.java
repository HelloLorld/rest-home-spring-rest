package com.company.controllers;

import com.company.entities.Book;
import com.company.entities.Buyer;
import com.company.exception.ResourceNotFound;
import com.company.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BuyerController {
    @Autowired
    private BuyerRepository repository;

    @GetMapping("/buyers")
    public List<Buyer> getAllBuyers() {
        return repository.findAll();
    }

    @GetMapping("/buyers/{id}")
    public ResponseEntity<?> getBuyer(@PathVariable(value = "id") Integer id) {
        Optional<Buyer> buyer = repository.findById(id);
        if (!buyer.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(buyer.get(), HttpStatus.OK);
    }

    @GetMapping("/districts_of_buyers")
    public Set<String> getCostAndNames() {
        List<Buyer> buyers = getAllBuyers();
        Set<String> setOfDistricts = new HashSet<>();
        for (Buyer buyer : buyers)
            setOfDistricts.add(buyer.getDistrict());
        return setOfDistricts;
    }

    @GetMapping("/discounts/{district}")
    public Map<String, Integer> getDiscountsOfBuyers(@PathVariable(value = "district") String district) {
        List<Buyer> buyers = getAllBuyers();
        Map<String, Integer> mapOfDiscounts = new HashMap<>();
        for (Buyer buyer : buyers)
            if (buyer.getDistrict().equals(district))
                mapOfDiscounts.put(buyer.getLastName(), buyer.getDiscount());
        return mapOfDiscounts;
    }

    @PostMapping("/buyers")
    public Buyer saveBuyer(@RequestBody Buyer buyer) {
        return repository.save(buyer);
    }

    @DeleteMapping("/buyers/{id}")
    public ResponseEntity<?> deleteBuyer(@PathVariable(value = "id") Integer id) {
        Optional<Buyer> buyer = repository.findById(id);
        if (!buyer.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else repository.delete(buyer.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/buers/{id}")
    public ResponseEntity<?> putOfBuyer(@PathVariable(value = "id") Integer id, @RequestBody Buyer buyerChanged) {
        Optional<Buyer> buyer = repository.findById(id);
        if (!buyer.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            buyerChanged.setId(buyer.get().getId());
            repository.save(buyerChanged);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("changed", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/buyers/{id}")
    public ResponseEntity<?> patchOfBuyer(@PathVariable(value = "id") Integer id, @RequestBody Buyer buyerChanged) {
        Optional<Buyer> buyer = repository.findById(id);
        boolean needChange = false;
        if (!buyer.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            buyerChanged.setId(buyer.get().getId());
            if (!buyer.get().getDistrict().equals(buyerChanged.getDistrict())) {
                needChange = true;
            }
            if (buyer.get().getDiscount() != buyerChanged.getDiscount()) {
                needChange = true;
            }
            if (!buyer.get().getLastName().equals(buyerChanged.getLastName())) {
                needChange = true;
            }
            if (needChange) repository.save(buyerChanged);
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
