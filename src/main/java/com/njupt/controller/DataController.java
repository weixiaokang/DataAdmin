package com.njupt.controller;

import com.njupt.model.*;
import com.njupt.model.bean.Article;
import com.njupt.model.bean.Journal;
import com.njupt.model.bean.Keyword;
import com.njupt.model.dao.ArticleDao;
import com.njupt.model.dao.JournalDao;
import com.njupt.model.dao.KeywordDao;
import com.njupt.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private MongoTemplate mongoTemplate;
    @GetMapping("/keyword")
    public List<KeywordModel> findAllKeyword() {
        Long count = 0L;
        List<Keyword> keywords = keywordDao.findAll();
        List<KeywordModel> keywordModels = new ArrayList<>();
        for (Keyword keyword : keywords) {
            Set<Article> articles = keyword.getArticle();
            if (articles == null)
                articles = new LinkedHashSet<>();
            int articleSize = articles.size();
            StringBuilder builder = new StringBuilder();
            articles.forEach(article -> builder.append(article.getName()).append(";"));
            String updatetime = "";
            if (articleSize > 0)
                updatetime = new ArrayList<>(articles).get(articleSize - 1).getTime();
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
            model.setSummary(article.getSummary());
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
            Set<Article> articles = journal.getArticle();
            if (articles != null) {
                model.setArticleNumber(articles.size());
                model.setArticle(new ArrayList<>(articles));
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

    @RequestMapping("/keyword/{limit}")
    public List<KeywordModel> keywordLimit(@PathVariable int limit) {
        List<Keyword> keywords = mongoTemplate.find(new Query().with(new Sort(Sort.Direction.DESC, "number")).limit(limit), Keyword.class);
        List<KeywordModel> keywordModels = new ArrayList<>();
        long count = 0;
        for (Keyword keyword : keywords) {
            Set<Article> articles = keyword.getArticle();
            if (articles == null)
                articles = new LinkedHashSet<>();
            int articleSize = articles.size();
            StringBuilder builder = new StringBuilder();
            articles.forEach(article -> builder.append(article.getName()).append(";"));
            String updatetime = "";
            if (articleSize > 0)
                updatetime = new ArrayList<>(articles).get(articleSize - 1).getTime();
            KeywordModel model = new KeywordModel(keyword.getKeyword(), keyword.getNumber());
            model.setArticle(builder.toString());
            model.setUpdatetime(updatetime);
            model.setId(++count);
            keywordModels.add(model);
        }
        return keywordModels;
    }

    @GetMapping("/journal/keyword/number")
    public DataModel journalKeywordBar() {
        DataModel model = new DataModel();
        List<Journal> journals = journalDao.findAll();
        List<String> x = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < journals.size(); i++) {
            data.add(0);
        }
        List<Set<String>> jaSet = new ArrayList<>();
        journals.forEach(journal -> {
            x.add(journal.getName());
            Set<Article> articles = journal.getArticle();
            Set<String> articleSet = new HashSet<>();
            articles.forEach(article -> articleSet.add(article.getName().trim()));
            jaSet.add(articleSet);
        });
        List<Keyword> keywords = keywordDao.findAll();
        for (int i = 0; i < keywords.size(); i++) {
            Set<Article> kaSet = keywords.get(i).getArticle();
            for (int j = 0; j < jaSet.size(); j++) {
                for (Article article : kaSet) {
                    if (jaSet.get(j).contains(article.getName().trim())) {
                        data.set(j, data.get(j) + 1);
                        break;
                    }
                }
            }
        }
        model.setX(x);
        model.setData(data);
        return model;
    }

    @GetMapping("/journal/article/number")
    public DataModel journalArticleBar() {
        DataModel model = new DataModel();
        List<Journal> journals = journalDao.findAll();
        List<String> x = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        journals.forEach(journal -> {
            x.add(journal.getName());
            Set<Article> articles = journal.getArticle();
            if (articles != null) {
                data.add(journal.getArticle().size());
            } else {
                data.add(0);
            }
        });
        model.setX(x);
        model.setData(data);
        return model;
    }

    @GetMapping("/article/time")
    public DataModel articleTimeLine() {
        List<Article> articles = mongoTemplate.find(new Query().with(new Sort(Sort.Direction.ASC, "time")), Article.class);
        Map<String, Integer> map = new TreeMap<>();
        DataModel model = new DataModel();
        articles.forEach(article -> {
            String time = article.getTime();
            if (DateUtils.isDateFormater(time)) {
                String key = article.getTime().substring(0, 7);
                map.put(key, map.getOrDefault(key, 0) + 1);
            }
        });
        model.setX(new ArrayList<>(map.keySet()));
        model.setData(new ArrayList<>(map.values()));
        return model;
    }
    @GetMapping("/keyword/time")
    public DataModel keywordTimeLine() {
        List<Keyword> keywords = keywordDao.findAll();
        DataModel model = new DataModel();
        Map<String, Integer> map = new TreeMap<>();
        keywords.forEach(keyword -> {
            List<Article> articles = new ArrayList<>(keyword.getArticle());
            String time = articles.get(articles.size() - 1).getTime();
            if (DateUtils.isDateFormater(time)) {
                String key = articles.get(articles.size() - 1).getTime().substring(0, 7);
                map.put(key, map.getOrDefault(key, 0) + 1);
            }
        });
        model.setX(new ArrayList<>(map.keySet()));
        model.setData(new ArrayList<>(map.values()));
        return model;
    }
    @GetMapping("/keyword/number/{limit}")
    public DataModel keywordBar(@PathVariable int limit) {
        List<Keyword> keywords = mongoTemplate.find(new Query().with(new Sort(Sort.Direction.DESC, "number")).limit(limit), Keyword.class);
        DataModel model = new DataModel();
        List<String> x = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        keywords.forEach(keyword -> {
            x.add(keyword.getKeyword());
            data.add(keyword.getNumber());
        });
        model.setX(x);
        model.setData(data);
        return model;
    }

    @GetMapping("/journal/keyword/per")
    public List<PieDataModel> journalKeywordPie() {
        List<PieDataModel> data = new ArrayList<>();
        List<Journal> journals = journalDao.findAll();
        List<Set<String>> jaSet = new ArrayList<>();
        journals.forEach(journal -> {
            PieDataModel model = new PieDataModel();
            model.setName(journal.getName());
            model.setValue(0);
            data.add(model);
            Set<Article> articles = journal.getArticle();
            Set<String> articleSet = new HashSet<>();
            articles.forEach(article -> articleSet.add(article.getName().trim()));
            jaSet.add(articleSet);
        });
        List<Keyword> keywords = keywordDao.findAll();
        for (int i = 0; i < keywords.size(); i++) {
            Set<Article> kaSet = keywords.get(i).getArticle();
            for (int j = 0; j < jaSet.size(); j++) {
                for (Article article : kaSet) {
                    if (jaSet.get(j).contains(article.getName().trim())) {
                        data.get(j).setValue(data.get(j).getValue() + 1);
                        break;
                    }
                }
            }
        }
        return data;
    }
}
