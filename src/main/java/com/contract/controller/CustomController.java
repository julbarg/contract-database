package com.contract.controller;

import com.contract.entities.custom.Custom;
import com.contract.entities.custom.Service;
import com.contract.exception.DataBaseResultNotFoundException;
import com.contract.exception.EmailAddressFormatIsWrongException;
import com.contract.util.ValidateEmail;
import com.sun.xml.internal.ws.api.message.Packet;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static com.contract.constant.CustomConstant.*;
import static com.contract.constant.ExceptionsConstant.*;

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
    public Custom insert(@RequestBody Custom custom) throws EmailAddressFormatIsWrongException, DuplicateKeyException {
        ValidateEmail email = new ValidateEmail();
        boolean validEmail = email.validate(custom.getContact().getEmail());
        if (validEmail == false) {
            throw new EmailAddressFormatIsWrongException();
        }
       try {
           mongoTemplate.insert(custom, CUSTOM);
       }catch (DuplicateKeyException e){
            throw new DuplicateKeyException(EMAIL_ADDRESS_ALREADY_EXISTS);
       }
        return custom;

    }

    @RequestMapping(value = FIND_BY_EMAIL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Document findByEmail(String email) throws DataBaseResultNotFoundException {
        AggregationOperation match = new MatchOperation(Criteria.where(CONTACT_EMAIL).is(email));
        AggregationOperation project = new ProjectionOperation().andExclude(ID).andInclude(NAME,LOCATION);
        Aggregation aggregation = Aggregation.newAggregation(match,project);
        AggregationResults<Document> output = mongoTemplate.aggregate(aggregation, CUSTOM, Document.class);
        List<Document> documentList = output.getMappedResults();
        if(documentList.isEmpty()){
            throw new DataBaseResultNotFoundException();
        }
        return documentList.get(0);
    }

    @RequestMapping(value = FIND_BY_EMAIL_SERVICES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HashMap> findByEmailServices(String email) throws DataBaseResultNotFoundException {
        AggregationOperation match = new MatchOperation(Criteria.where(CONTACT_EMAIL).is(email));
        AggregationOperation project = new ProjectionOperation().andExclude(ID).andInclude(SERVICES);
        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<HashMap> results = mongoTemplate.aggregate(aggregation, CUSTOM, HashMap.class);
        List<HashMap> hashMapList = results.getMappedResults();
        if(hashMapList.isEmpty()){
            throw new DataBaseResultNotFoundException();
        }
        return hashMapList;
    }

    @RequestMapping(value = FIND_BY_EMAIL_SERVICE_BY_STATUS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HashMap> findByEmailServiceStatus(String email, String status) throws DataBaseResultNotFoundException {
        AggregationOperation match = new MatchOperation(Criteria.where(CONTACT_EMAIL).is(email)
                                                                .andOperator(Criteria.where(SERVICE_STATUS).is(status)));
        AggregationOperation project = new ProjectionOperation().andExclude(ID).andInclude(SERVICES);
        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<HashMap> results = mongoTemplate.aggregate(aggregation, CUSTOM, HashMap.class);
        List<HashMap> hashMapList = results.getMappedResults();
        if(hashMapList.isEmpty()){
            throw new DataBaseResultNotFoundException();
        }
        return hashMapList;
    }
}






















