package com.njupt.model;


/**
 * @author weixk
 * @version Created time 17/5/6. Last-modified time 17/5/6.
 */
public class ArticleModel {
    private Long id;
    private String name;
    private String author;
    private String time;
    private String url;

    public ArticleModel() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
