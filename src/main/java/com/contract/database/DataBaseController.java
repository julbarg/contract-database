package com.contract.database;

import com.contract.entities.Category;
import com.contract.entities.Custom;
import com.contract.repository.CategoryRepository;
import com.contract.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by anggomez1 on 5/3/17.
 */
@RestController
public class DataBaseController {


    //@Autowired
   // private CategoryRepository categoryRepository1;

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

  /*  @RequestMapping(value = "/category/findAll2", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> findAllCategories() {
        List<Category> categoryList = categoryRepository1.findAll();
        for (Category category : categoryList) {
            System.out.println(category);
        }
        return categoryList;
    }*/

    @RequestMapping(value = "/custom/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Custom findEmailPassword(@RequestBody Custom custom) {
        String email = custom.getContact().getEmail();
        String password = custom.getPassword();
        List<Custom> customRepo = customRepository.findByContact_EmailAndPassword(email, password);


        return customRepo.size() > 0 ? customRepo.get(0) : null;
    }

    @RequestMapping(value = "/custom/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

    public Custom insertCustom(@RequestBody Custom custom) {
        Custom newCustom = customRepository.insert(custom);
        return newCustom;
    }

    @RequestMapping(value = "/custom/updateCustom", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)

    public Custom updateCustom(String idCustom, @RequestBody Custom custom) {

        if (idCustom != null) {
            Custom customFindId = customRepository.findById(idCustom);
            if (customFindId != null) {
                //custom.setId(idCustom);
                custom = customRepository.save(custom);
                return custom;
            }

        }
        return null;
    }

    @RequestMapping(value = "/category/findAllMongoTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> findAllCategoriesMongoTemplate() {
        List<Category> categoryList = mongoTemplate.findAll(Category.class);
        for (Category category : categoryList) {
            System.out.println(category);
        }
        return categoryList;
    }


}
