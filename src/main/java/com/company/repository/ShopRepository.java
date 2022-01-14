package com.company.repository;

import com.company.entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop,Integer> {
    @Query(value = "SELECT s.name FROM shops s where s.district = :district", nativeQuery = true)
    List<String> getByDistrict(@Param("district") String district);
}
