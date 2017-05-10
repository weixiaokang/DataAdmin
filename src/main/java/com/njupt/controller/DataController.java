package com.njupt.controller;

import com.njupt.model.ArticleModel;
import com.njupt.model.JournalModel;
import com.njupt.model.KeywordModel;
import com.njupt.model.bean.Article;
import com.njupt.model.bean.Journal;
import com.njupt.model.bean.Keyword;
import com.njupt.model.dao.ArticleDao;
import com.njupt.model.dao.JournalDao;
import com.njupt.model.dao.KeywordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weixk
 * @version Created time 17/5/2. Last-modified time 17/5/2.
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private KeywordDao keywordDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private JournalDao journalDao;
    @GetMapping("/keyword")
    public List<KeywordModel> findAllKeyword() {
        Long count = 0L;
        List<Keyword> keywords = keywordDao.findAll();
        List<KeywordModel> keywordModels = new ArrayList<>();
        for (Keyword keyword : keywords) {
            List<Article> articles = keyword.getArticle();
            int articleSize = articles.size();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < articleSize; i++) {
                builder.append(articles.get(i).getName()).append(";");
            }
            String updatetime = "";
            if (articleSize > 0) {
                updatetime = articles.get(articleSize - 1).getTime();
            }
            KeywordModel model = new KeywordModel(keyword.getKeyword(), keyword.getNumber());
            model.setArticle(builder.toString());
            model.setUpdatetime(updatetime);
            model.setId(++count);
            keywordModels.add(model);
        }
        return keywordModels;
    }
    @GetMapping("/keyword/size")
    public long keywordSize() {
        return keywordDao.count();
    }
    @PostMapping("/keyword")
    public Keyword addKeyword(@RequestBody Keyword keyword) {
        return keywordDao.save(keyword);
    }
    @DeleteMapping("/keyword")
    public String deleteKeyword(@RequestBody List<KeywordModel> keywordModels) {
        long beforeCount = keywordDao.count();
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 0; i < keywordModels.size(); i++) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(keywordModels.get(i).getKeyword());
            keyword.setNumber(keywordModels.get(i).getNumber());
            keyword.setArticle(null);
            keywords.add(keyword);
        }
        keywordDao.delete(keywords);
        long afterCount = keywordDao.count();
        if (beforeCount - afterCount > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("/article")
    public List<ArticleModel> findAllArticle() {
        Long count = 0L;
        List<Article> articles = articleDao.findAll();
        List<ArticleModel> articleModels = new ArrayList<>();
        for (Article article : articles) {
            ArticleModel model = new ArticleModel();
            model.setId(++count);
            model.setName(article.getName());
            model.setTime(article.getTime());
            model.setUrl(article.getUrl());
            StringBuilder builder = new StringBuilder();
            if (article.getAuthor() != null) {
                for (String author : article.getAuthor()) {
                    builder.append(author).append(";");
                }
            }
            model.setAuthor(builder.toString());
            articleModels.add(model);
        }
        return articleModels;
    }
    @GetMapping("/article/size")
    public long articleSize() {
        return articleDao.count();
    }
    @PostMapping("/article")
    public Article addArticle(@RequestBody Article article) {
        return articleDao.save(article);
    }
    @DeleteMapping("/article")
    public String deleteArticle(@RequestBody List<ArticleModel> articleModels) {
        long beforeCount = articleDao.count();
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < articleModels.size(); i++) {
            Article article = new Article();
            article.setName(articles.get(i).getName());
            article.setTime(articles.get(i).getTime());
            article.setUrl(articles.get(i).getUrl());
            article.setAuthor(null);
            articles.add(article);
        }
        articleDao.delete(articles);
        long afterCount = articleDao.count();
        if (beforeCount - afterCount > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
    @GetMapping("/journal")
    public List<JournalModel> findAllJournal() {
        Long count = 0L;
        List<Journal> journals = journalDao.findAll();
        List<JournalModel> journalModels = new ArrayList<>();
        for (Journal journal : journals) {
            JournalModel model = new JournalModel();
            model.setId(++count);
            model.setName(journal.getName());
            model.setUrl(journal.getUrl());
            model.setCreatetime(journal.getCreatetime());
            List<Article> articles = journal.getArticle();
            if (articles != null) {
                model.setArticleNumber(articles.size());
                model.setArticle(articles);
            }
            journalModels.add(model);
        }
        return journalModels;
    }
    @GetMapping("/journal/size")
    public long journalSize() {
        return journalDao.count();
    }
    @PostMapping("/journal")
    public Journal addJournal(@RequestBody Journal journal) {
        return journalDao.save(journal);
    }
    @DeleteMapping("/journal")
    public String deleteJournal(@RequestBody List<JournalModel> journalModels) {
        long beforeCount = journalDao.count();
        List<Journal> journals = new ArrayList<>();
        for (int i = 0; i < journalModels.size(); i++) {
            Journal journal = new Journal();
            journal.setName(journalModels.get(i).getName());
            journal.setUrl(journalModels.get(i).getUrl());
            journal.setArticle(null);
            journals.add(journal);
        }
        journalDao.delete(journals);
        long afterCount = journalDao.count();
        if (beforeCount - afterCount > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
}
