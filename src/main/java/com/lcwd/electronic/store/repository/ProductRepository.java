package com.lcwd.electronic.store.repository;

import com.lcwd.electronic.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , String> {

    Page<Product> findByTitleContaining(Pageable pageable,String subString);

    Page<Product> findByLiveTrue(Pageable pageable);

}
