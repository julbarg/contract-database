package com.contract.controller;

import com.contract.entities.Category;
import com.contract.entities.Name;
import com.contract.enums.CategoryStatus;
import com.contract.exception.DataBaseResultNotFoundException;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.contract.constant.CategoryConstant.*;
import static com.contract.constant.ExceptionsConstant.DATABASE_NO_RECORDS_UPDATE_EXCEPTION_MESSAGE;

/**
 * Created by anggomez1 on 5/11/17.
 */
@RestController
public class CategoryController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = FIND_ACTIVE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> findActive() throws DataBaseResultNotFoundException {
        Query query = new Query(Criteria.where(STATUS).is(CategoryStatus.ACTIVE.getStatus()));
        List<Category> categoryList = mongoTemplate.find(query, Category.class);
        if(categoryList.isEmpty())
            throw new DataBaseResultNotFoundException();
        return categoryList;
    }

    @RequestMapping(value = FIND_NAME_BY_LANGUAGUES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HashMap> findNamesByLanguages(String language) throws DataBaseResultNotFoundException {
        AggregationOperation unWind = new UnwindOperation(Fields.field("names"));
        AggregationOperation match = new MatchOperation(Criteria.where("names.language").is(language)
                .andOperator(Criteria.where(STATUS).is(CategoryStatus.ACTIVE.getStatus())));
        AggregationOperation project = new ProjectionOperation()
                .and("names.name").as("name")
                .andExclude("_id");
        Sort sortName = new Sort(Sort.Direction.ASC, "name");
        AggregationOperation sort = new SortOperation(sortName);
        Aggregation aggregation = Aggregation.newAggregation(unWind, match, project, sort);
        AggregationResults<HashMap> output = mongoTemplate.aggregate(aggregation, CATEGORIES, HashMap.class);
        List<HashMap> hashMapList = output.getMappedResults();
        if(hashMapList.isEmpty())
            throw new DataBaseResultNotFoundException();
        return hashMapList;
    }

    @RequestMapping(value = INSERT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category insertCategory(@RequestBody Category category) {
        category.setStatus(CategoryStatus.ACTIVE.getStatus());
        mongoTemplate.insert(category, CATEGORIES);
        return category;
    }

    @RequestMapping(value = UPDATE_BY_LANGUAGES_NAME, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WriteResult updateByLanguageAndName(String value, @RequestBody Name name) throws DataBaseResultNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where(DEFAULT_VALUE).is(value)
                .andOperator(Criteria.where("names.language").is(name.getLanguage())));

        Update update = new Update();
        Update set = update.set("names.$.name", name.getName());
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, CATEGORIES);
        if(writeResult.getN() == 0)
            throw new DataBaseResultNotFoundException();
        return writeResult;

    }

    @RequestMapping(value = UPDATE_INSERT_BY_LANGUAGES_NAME , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WriteResult updateInsertByLanguageName(String value, @RequestBody Name... names) {
        Query query = new Query();
        query.addCriteria(Criteria.where(DEFAULT_VALUE).is(value));
        Update update = new Update();
        update.pushAll("names", names);
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, CATEGORIES);
        return writeResult;
    }

    @RequestMapping(value = UPDATE_BY_STATUS, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WriteResult updateByStatus(@RequestBody Category category) {
        Query query = new Query(Criteria.where(DEFAULT_VALUE).is(category.getDefaultValue()));
        Update update = new Update();
        update.set(STATUS, category.getStatus());
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Category.class);
        return writeResult;
    }

    @RequestMapping(value = INSERT_MANY, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insert(@RequestBody Category... listCategory) {
        Collection<Category> categoryCollection = new LinkedList<>();
        for(Category category: listCategory) {
            categoryCollection.add(category);
        }
        mongoTemplate.insert(categoryCollection, CATEGORIES);
        return "Success";
    }

}
