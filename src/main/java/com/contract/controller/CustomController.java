package com.contract.controller;

import com.contract.entities.custom.Custom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.contract.constant.CustomConstant.*;

/**
 * Created by anggomez1 on 5/31/17.
 */

@RestController
public class CustomController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = FIND_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public List<Custom> findAll(){
       List<Custom> customList = mongoTemplate.findAll(Custom.class);
       return customList;
    }


    @RequestMapping(value = INSERT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Custom insert(@RequestBody Custom custom){
        //if(custom.getContact().getEmail())
        mongoTemplate.insert(custom, CUSTOM );
        return custom;
    }
}
