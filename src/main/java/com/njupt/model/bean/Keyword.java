package com.njupt.model.bean;


import org.springframework.data.annotation.Id;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author weixk
 * @version Created time 17/4/13. Last-modified time 17/4/13.
 */
public class Keyword {

    @Id
    private String keyword;
    private Integer number;
    private List<Article> article;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }
}
