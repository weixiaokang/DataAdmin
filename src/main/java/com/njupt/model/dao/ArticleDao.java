package com.njupt.model.dao;

import com.njupt.model.bean.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author weixk
 * @version Created time 17/5/4. Last-modified time 17/5/4.
 */
public interface ArticleDao extends MongoRepository<Article, String>{

}
