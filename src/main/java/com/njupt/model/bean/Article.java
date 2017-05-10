package com.njupt.model.bean;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author weixk
 * @version Created time 17/5/2. Last-modified time 17/5/2.
 */
public class Article {
    @Id
    private String name;
    private List<String> author;
    private String time;
    private String url;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
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
