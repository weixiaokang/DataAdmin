package com.njupt.controller;

import com.njupt.model.bean.Article;
import com.njupt.model.bean.Journal;
import com.njupt.model.bean.Keyword;
import com.njupt.model.dao.ArticleDao;
import com.njupt.model.dao.JournalDao;
import com.njupt.model.dao.KeywordDao;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author weixk
 * @version Created time 17/4/30. Last-modified time 17/4/30.
 */
@RestController
@RequestMapping("/spider")
@Validated
public class SpiderController {

    private static final Logger log = LoggerFactory.getLogger(SpiderController.class);

    private int total = 20 * 10;
    @Autowired
    private KeywordDao keywordDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private JournalDao journalDao;
    private int process = 0;
    @RequestMapping("/search")
    public String search(@NotEmpty(message = "期刊名称不能为空") @RequestParam String name, @URL @RequestParam String url) {
        log.info("name=" + name + "; url=" + url);
        process = 0;
        new Thread(() -> {
            Journal journal = new Journal(name, url);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            journal.setCreatetime(time);
            WebDriver webDriver = new PhantomJSDriver();
            List<Keyword> keywords = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < 10; i++) {
                webDriver.get(url);
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WebElement nextPage = webDriver.findElement(By.linkText(String.valueOf(i + 1)));
                nextPage.click();
                try {
                    Thread.sleep(5 * 1000);
                    List<WebElement> nameElements = webDriver.findElements(By.xpath("//span[@class='name']/a"));
                    List<WebElement> authorElements = webDriver.findElements(By.xpath("//span[@class='author']"));
                    List<WebElement> timeElements = webDriver.findElements(By.xpath("//span[@class='company']"));
                    List<Article> articles = new ArrayList<>();
                    for (int j = 0; j < nameElements.size(); j++) {
                        Article article = new Article();
                        article.setTime(timeElements.get(j).getText());
                        article.setName(nameElements.get(j).getText());
                        String href = nameElements.get(j).getAttribute("href");
                        Pattern pattern = Pattern.compile("dbCode=\\w+&filename=\\w+&tableName=\\w+");
                        Matcher matcher = pattern.matcher(href);
                        String nextUrl = "http://kns.cnki.net/kcms/detail/detail.aspx?";
                        if (matcher.find()) {
                            nextUrl += matcher.group();
                        }
                        article.setUrl(nextUrl);
                        String[] authors = authorElements.get(j).getText().split(";\\s*");
                        article.setAuthor(Arrays.asList(authors));
                        articles.add(article);
                        articleDao.save(article);
                        log.info("==article==" + nameElements.get(j).getText());
                    }
                    List<Article> journalArticle = journal.getArticle();
                    if (journalArticle == null) {
                        journalArticle = new ArrayList<>();
                    }
                    journalArticle.addAll(articles);
                    journal.setArticle(journalArticle);
                    for (int j = 0; j < articles.size(); j++) {
                        count++;
                        process = (int)((float)count / total * 100);
                        webDriver.get(articles.get(j).getUrl());
                        try {
                            Thread.sleep(5 * 1000);
                            List<WebElement> keywordElements = webDriver.findElements(By.xpath("//a[contains(@onclick, 'kw')]"));
                            for (int k = 0; k < keywordElements.size(); k++) {
                                String keywordName = keywordElements.get(k).getText().replace(";", "");
                                Keyword keyword = keywordDao.findOne(keywordName);
                                if (keyword == null) {
                                    keyword = new Keyword();
                                    keyword.setKeyword(keywordName);
                                    keyword.setNumber(0);
                                    keyword.setArticle(new ArrayList<>());
                                }
                                keyword.setNumber(keyword.getNumber() + 1);
                                List<Article> articleList = keyword.getArticle();
                                if (articleList == null)
                                    articleList = new ArrayList<>();
                                articleList.add(articles.get(j));
                                keyword.setArticle(articleList);
                                keywords.add(keyword);
                                keywordDao.save(keyword);
                                log.info("==keyword==" + keywordName);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            journalDao.save(journal);
            process = 100;
            log.info(String.valueOf(keywords.size()));
            for (Keyword keyword : keywords) {
                log.info("=======" + keyword.getKeyword() + " " + keyword.getNumber() + "=======");
                for (Article article : keyword.getArticle()) {
                    log.info("    " + article.getName() + " " + article.getTime());
                    List<String> authors = article.getAuthor();
                    if (authors != null) {
                        for (String author : authors) {
                            log.info("        " + author);
                        }
                    }
                }
            }
            webDriver.close();
            webDriver.quit();
        }).start();
        return "提交成功";
    }

    @RequestMapping("/process")
    public int process() {
        return process;
    }
    @RequestMapping("/test")
    public String test(@NotEmpty @URL @RequestParam String url) {
        return url;
    }
}
