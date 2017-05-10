package com.njupt.model;

/**
 * @author weixk
 * @version Created updatetime 17/5/5. Last-modified updatetime 17/5/5.
 */
public class KeywordModel {
    private Long id;
    private String keyword;
    private Integer number;
    private String article;
    private String updatetime;

    public KeywordModel() {

    }

    public KeywordModel(String keyword, Integer number) {
        this.keyword = keyword;
        this.number = number;
        this.updatetime = updatetime;
    }

    public KeywordModel(String keyword, Integer number, String article, String updatetime) {
        this.keyword = keyword;
        this.number = number;
        this.article = article;
        this.updatetime = updatetime;
    }

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

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
