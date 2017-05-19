package com.contract.repository;

import com.contract.entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by anggomez1 on 5/7/17.
 */
@Repository
public interface CategoryRepository  extends MongoRepository<Category, String>{

    // public List<Category> findByName(String name);
}
