package com.example.CRUDApplication.repo;

import com.example.CRUDApplication.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;

@Repository
public interface BookRepo  extends JpaRepository<Book, Long> {
    List<Book> findAll(Sort sort);
    List<Book> findByTitleContainingIgnoreCase(String title);
}
