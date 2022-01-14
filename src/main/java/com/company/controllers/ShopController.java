package com.company.controllers;

import com.company.entities.Shop;
import com.company.exception.ResourceNotFound;
import com.company.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ShopController {
    @Autowired
    private ShopRepository repository;

    @GetMapping("/shops")
    public List<Shop> getAllShops() {
        return repository.findAll();
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<?> getShop(@PathVariable(value = "id") Integer id) {
        Optional<Shop> shop = repository.findById(id);
        if (!shop.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(shop.get(), HttpStatus.OK);
    }

    @GetMapping("/shop/{district}")
    public List<String> getShopsByDistrict(@PathVariable(value = "district") String district) {
        List<String> list = repository.getByDistrict(district);
        return list;
    }

    @PostMapping("/shops")
    public Shop saveShop(@RequestBody Shop shop) {
        return repository.save(shop);
    }

    @DeleteMapping("/shops/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable(value = "id") Integer id) {
        Optional<Shop> shop = repository.findById(id);
        if (!shop.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else repository.delete(shop.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/shops/{id}")
    public ResponseEntity<?> putOfShop(@PathVariable(value = "id") Integer id, @RequestBody Shop shopChanged) {
        Optional<Shop> shop = repository.findById(id);
        if (!shop.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            shopChanged.setId(shop.get().getId());
            repository.save(shopChanged);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("changed", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/shops/{id}")
    public ResponseEntity<?> patchOfShop(@PathVariable(value = "id") Integer id, @RequestBody Shop shopChanged) {
        Optional<Shop> buyer = repository.findById(id);
        boolean needChange = false;
        if (!buyer.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Buyer not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            shopChanged.setId(buyer.get().getId());
            if (!buyer.get().getDistrict().equals(shopChanged.getDistrict())) {
                needChange = true;
            }
            if (buyer.get().getCommission() != shopChanged.getCommission()) {
                needChange = true;
            }
            if (!buyer.get().getName().equals(shopChanged.getName())) {
                needChange = true;
            }
            if (needChange) repository.save(shopChanged);
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
