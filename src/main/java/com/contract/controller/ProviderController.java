package com.contract.controller;

import ch.qos.logback.classic.spi.ThrowableProxyVO;
import com.contract.entities.Category;
import com.contract.entities.provider.Curriculum;
import com.contract.entities.provider.ProvideServices;
import com.contract.entities.provider.Provider;
import com.contract.entities.provider.Services;
import com.contract.exception.DataBaseResultNotFoundException;
import com.mongodb.WriteResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

import static com.contract.constant.CategoryConstant.DEFAULT_VALUE;
import static com.contract.constant.CategoryConstant.STATUS;
import static com.contract.constant.ProviderConstant.*;

/**
 * Created by julbarra on 5/31/17.
 */
@RestController
public class ProviderController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = INSERT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Provider insert(@RequestBody Provider provider) {
        mongoTemplate.insert(provider, PROVIDERS);
        return provider;
    }

    @RequestMapping(value = FIND_BY_EMAIL, method = RequestMethod.GET)
    public Document findByEmail(String email) throws DataBaseResultNotFoundException {
        AggregationOperation match = new MatchOperation(Criteria.where(EMAIL).is(email));
        AggregationOperation project = new ProjectionOperation().andExclude("_id")
                                                                .andInclude(NAME, "location")
                                                                .andExpression("curriculum.services").as("services");

        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<Document> output = mongoTemplate.aggregate(aggregation, PROVIDERS, Document.class);

        List<Document> documentList = output.getMappedResults();
        if(documentList.isEmpty())
            throw new DataBaseResultNotFoundException();

        return documentList.get(0);
    }

    @RequestMapping(value = FIND_BY_SERVICE, method = RequestMethod.GET)
    public Document findByService(String service) throws DataBaseResultNotFoundException {
        AggregationOperation match = new MatchOperation(Criteria.where(CATEGORIES).is(service));
        AggregationOperation project = new ProjectionOperation().andExclude("_id")
                .andInclude(NAME, "location", "curriculum.services");

        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<Document> output = mongoTemplate.aggregate(aggregation, PROVIDERS, Document.class);

        List<Document> documentList = output.getMappedResults();
        if(documentList.isEmpty())
            throw new DataBaseResultNotFoundException();

        return documentList.get(0);
    }

    @RequestMapping(value = FIND_BY_GEOLOCATION, method = RequestMethod.POST)
    public List<Provider> findByGeolocation(@RequestBody Point point) {
        ArrayList<Double> coordinates = new ArrayList<Double>();
        coordinates.add(point.getY());
        coordinates.add(point.getX());

        Criteria criteria = Criteria.where("location.geolocation").is(new Document("$near",
                new Document("$geometry",
                        new Document("type", "Point")
                                .append("coordinates", coordinates))
                        .append("$maxDistance",MAX_DISTANCE * Metrics.KILOMETERS.getMultiplier())));
        Query query = new Query(criteria);


        List<Provider> providerList = mongoTemplate.find(query, Provider.class);

        return providerList;
    }

    @RequestMapping(value = UPDATE_PROVIDER, method = RequestMethod.POST)
    public WriteResult updateProvider(@RequestBody Provider provider, String email) {
        Query query = new Query(Criteria.where(EMAIL).is(email));
        Update update = new Update();
        update.set(NAME, provider.getName());
        update.set("contact.phone", provider.getContact().getPhone());
        update.set("location", provider.getLocation());

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Provider.class);

        return writeResult;
    }

    @RequestMapping(value = INSERT_SERVICE, method = RequestMethod.POST)
    public WriteResult insertService(@RequestBody Services service, String email) {
        Query query = new Query(Criteria.where(EMAIL).is(email));
        Update update = new Update();
        update.push("curriculum.$.services", service);

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Provider.class);

        return writeResult;
    }

    @RequestMapping(value = UPDATE_SERVICE, method = RequestMethod.POST)
    public WriteResult updateService(@RequestBody Services services, String email, String name) {
        Query query = new Query(Criteria.where(EMAIL).is(email).and("curriculum.services.name").is(name));
        Update update = new Update();
        update.set("curriculum.services.$", services);

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, PROVIDERS);

        return writeResult;
    }

    @RequestMapping(value = INSERT_PROVIDER_SERVICE, method = RequestMethod.POST)
    public WriteResult insertProviderService(@RequestBody ProvideServices provideServices, String email) {
        Query query = new Query(Criteria.where(EMAIL).is(email));
        Update update = new Update();
        update.push("provideServices", provideServices);

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Provider.class);

        return writeResult;
    }

    @RequestMapping(value = UPDATE_PROVIDER_SERVICE, method = RequestMethod.POST)
    public WriteResult insertProviderService(@RequestBody ProvideServices provideServices, String email, String emailCustom) {
        Query query = new Query(Criteria.where(EMAIL).is(email).and("provideServices.custom").is(emailCustom));
        Update update = new Update();
        update.set("provideServices.$", provideServices);

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Provider.class);

        return writeResult;
    }

    @RequestMapping(value = UPDATE_CURRICULUM, method = RequestMethod.POST)
    public WriteResult updateCurriculum(@RequestBody Curriculum curriculum, String email) {
        Query query = new Query(Criteria.where(EMAIL).is(email));
        Update update = new Update();
        update.set("curriculum", curriculum);

        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Provider.class);

        return writeResult;
    }

}
