package com.njupt.model;

import com.njupt.model.bean.Article;

import java.util.List;

/**
 * @author weixk
 * @version Created time 17/5/6. Last-modified time 17/5/6.
 */
public class JournalModel {
    private Long id;
    private String name;
    private List<Article> article;
    private Integer articleNumber;
    private String url;
    private String createtime;

    public JournalModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }

    public Integer getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(Integer articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
