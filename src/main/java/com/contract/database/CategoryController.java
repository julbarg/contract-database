package com.contract.database;

import com.contract.entities.Category;
import com.contract.entities.Name;
import com.contract.enums.CategoryStatus;
import com.mongodb.DBObject;
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

/**
 * Created by anggomez1 on 5/11/17.
 */
@RestController
public class CategoryController {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final static String CATEGORIES = "categories";


    @RequestMapping(value = "category/findActive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> findActive() {
        List<Category> categoryList = mongoTemplate.findAll(Category.class);
        Query query = new Query(Criteria.where(Category.STATUS).is(CategoryStatus.ACTIVE.getStatus()));
        mongoTemplate.find(query, Category.class);

        return categoryList;
    }

    @RequestMapping(value = "category/findNamesByLanguages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HashMap> findNamesByLanguages(String language) {

        AggregationOperation unWind = new UnwindOperation(Fields.field("names"));
        AggregationOperation match = new MatchOperation(Criteria.where("names.language").is(language)
                .andOperator(Criteria.where("status").is(CategoryStatus.ACTIVE.getStatus())));
        AggregationOperation project = new ProjectionOperation()
                .and("names.name").as("name")
                .andExclude("_id");
        Sort sortName = new Sort(Sort.Direction.ASC, "name");
        AggregationOperation sort = new SortOperation(sortName);
        Aggregation aggregation = Aggregation.newAggregation(unWind, match, project, sort);
        AggregationResults<HashMap> output = mongoTemplate.aggregate(aggregation, CATEGORIES, HashMap.class);

        return output.getMappedResults();
    }

    @RequestMapping(value = "/category/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

    public Category insertCategory(@RequestBody Category category) {
        mongoTemplate.insert(category, CATEGORIES);

        return category;
    }

    @RequestMapping(value = "/category/updateByLanguageAndValue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WriteResult updateByLanguageAndValue(String value, @RequestBody Name name) {

        Query query = new Query();
        query.addCriteria(Criteria.where("defaultValue").is(value)
                .andOperator(Criteria.where("names.language").is(name.getLanguage())));

        Update update = new Update();
        Update set = update.set("names.$.name", name.getName());
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, CATEGORIES);
        return writeResult;

    }

    @RequestMapping(value = "/category/updateInsertByLanguageName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WriteResult updateInsertByLanguageName(String value, @RequestBody Name... names) {
        Query query = new Query();
        query.addCriteria(Criteria.where("defaultValue").is(value));
        Update update = new Update();
        update.pushAll("names", names);
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, CATEGORIES);
        return writeResult;
    }

    @RequestMapping(value = "/category/updateByStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WriteResult updateByStatus(@RequestBody Category category) {
        Query query = new Query(Criteria.where("defaultValue").is(category.getDefaultValue()));
        Update update = new Update();
        update.set("status", category.getStatus());
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, Category.class);
        return writeResult;
    }



    @RequestMapping(value = "/category/insertMany", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insert(@RequestBody Category... listCategory) {
        Collection<Category> categoryCollection = new LinkedList<>();
        for(Category category: listCategory) {
            categoryCollection.add(category);
        }
        mongoTemplate.insert(categoryCollection, CATEGORIES);

        return "Success";
    }

}
