package com.contract.repository;

import com.contract.entities.Custom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by anggomez1 on 5/7/17.
 */
@Repository
public interface CustomRepository extends MongoRepository<Custom, String> {

    List<Custom> findByContact_EmailAndPassword(String email, String password);
    Custom findById(String id);
}

