package com.contract.controller;

import com.contract.entities.provider.Provider;
import com.contract.exception.DataBaseResultNotFoundException;
import com.contract.util.DateUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
        Query query = new Query(Criteria.where(EMAIL).is(email));
        Provider provider = mongoTemplate.findOne(query, Provider.class);
        if(provider == null)
            throw new DataBaseResultNotFoundException();

        Document document = new Document();
        document.append("name", provider.getName())
            .append("contact", provider.getContact())
            .append("location", provider.getLocation())
            .append("services", provider.getCurriculum().getServices());

        String experience = getExperience(provider.getCurriculum().getExperience().getStartDate());
        if(experience != null)
            document.append("experience", experience);

        return document;
    }

    private String getExperience(Date startDate) {
        try {
            LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long experienceYears = DateUtil.getDiffYears(LocalDate.now(), localStartDate);
            if(experienceYears > 0) {
                return experienceYears + " years";
            } else {
                long experienceMonths = DateUtil.getDiffMonths(LocalDate.now(), localStartDate);
                if(experienceMonths > 0) {
                    return experienceMonths + " months";
                }
            }
        } catch (Exception e) {}

        return null;
    }
}
