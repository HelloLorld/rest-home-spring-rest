package com.company.repository;

import com.company.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BookRepository extends JpaRepository<Book,Integer> {
/*    @Query(value = "SELECT s.name, s.price FROM books s where s.price >= :price GROUP BY s.name,s.price ORDER BY s.price", nativeQuery = true)
    Map<String, Integer> getBooksByPrice(@Param("price") Integer price);*/
}
