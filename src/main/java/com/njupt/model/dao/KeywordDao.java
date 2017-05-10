package com.njupt.model.dao;

import com.njupt.model.bean.Keyword;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author weixk
 * @version Created time 17/4/13. Last-modified time 17/4/13.
 */
public interface KeywordDao extends MongoRepository<Keyword, String>{

}
