package com.njupt.model.bean;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author weixk
 * @version Created time 17/5/4. Last-modified time 17/5/4.
 */
public class Journal {
    @Id
    private String name;
    private List<Article> article;
    private String url;
    private String createtime;

    public Journal() {

    }
    public Journal(String name, String url) {
        this.name = name;
        this.url = url;
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
