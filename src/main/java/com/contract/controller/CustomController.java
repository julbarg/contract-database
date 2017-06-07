package com.contract.controller;

import com.contract.entities.custom.Custom;
import com.contract.exception.EmailAddressFormatIsWrong;
import com.contract.exception.EmailAddressIsExist;
import com.contract.util.ValidateEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public List<Custom> findAll() {
        List<Custom> customList = mongoTemplate.findAll(Custom.class);
        return customList;
    }

    @RequestMapping(value = INSERT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Custom insert(@RequestBody Custom custom) throws EmailAddressFormatIsWrong, EmailAddressIsExist {
        ValidateEmail email = new ValidateEmail();
        boolean validEmail = email.validate(custom.getContact().getEmail());
        Query query = new Query(Criteria.where(EMAIL).is(custom.getContact().getEmail()));
        Custom findEmail = mongoTemplate.findOne(query, Custom.class);

        if (validEmail == false) {
            throw new EmailAddressFormatIsWrong();
        }else{
        if (findEmail != null) {

            throw new EmailAddressIsExist();
        }
        else {
            mongoTemplate.insert(custom, CUSTOM);
        }
        return custom;
        }
    }


}
