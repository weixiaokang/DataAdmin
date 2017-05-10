package com.njupt;

import com.njupt.model.bean.Article;
import com.njupt.model.bean.Journal;
import com.njupt.model.bean.Keyword;
import com.njupt.model.dao.ArticleDao;
import com.njupt.model.dao.JournalDao;
import com.njupt.model.dao.KeywordDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AppTest {

    @Autowired
    private KeywordDao keywordDao;
    @Autowired
    private JournalDao journalDao;
    @Autowired
    private ArticleDao articleDao;
    /**
     * Rigourous Test :-)
     */
//    @Test
//    public void testApp() {
//        RestTemplate restTemplate = new RestTemplate();
//        List keywords = restTemplate.getForObject("http://127.0.0.1:8080/data/keyword", List.class);
//        System.out.println(keywords.toString());
//        Keyword temp = new Keyword();
//        temp.setKeyword("hello");
//        temp.setNumber(1);
//        temp.setArticle(new ArrayList<>());
//        Keyword keyword = restTemplate.postForObject("http://127.0.0.1:8080/data/keyword", temp, Keyword.class);
//    }

    @Test
    public void testMongo() {
        String journalUrl = "http://navi.cnki.net/knavi/JournalDetail?pcode=CJFD&pykm=RJXB";
        String journalName = "软件学报";
        Journal journal = new Journal(journalName, journalUrl);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        journal.setCreatetime(time);
        WebDriver webDriver = new PhantomJSDriver();
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
//            webDriver.get("http://navi.cnki.net/knavi/JournalDetail?pcode=CJFD&pykm=RJXB");
            webDriver.get(journalUrl);
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
                    System.err.println("==article==" + nameElements.get(j).getText());
                }
                List<Article> journalArticle = journal.getArticle();
                if (journalArticle == null) {
                    journalArticle = new ArrayList<>();
                }
                journalArticle.addAll(articles);
                journal.setArticle(journalArticle);
                for (int j = 0; j < articles.size(); j++) {
                    webDriver.get(articles.get(j).getUrl());
                    try {
                        Thread.sleep(5 * 1000);
                        List<WebElement> keywordElements = webDriver.findElements(By.xpath("//a[contains(@onclick, 'kw')]"));
                        for (int k = 0; k < keywordElements.size(); k++) {
                            String name = keywordElements.get(k).getText().replace(";", "");
                            System.err.println("====keyword====" + name);
                            Keyword keyword = keywordDao.findOne(name);
                            if (keyword == null) {
                                keyword = new Keyword();
                                keyword.setKeyword(name);
                                keyword.setNumber(0);
                                keyword.setArticle(new ArrayList<>());
                            }
                            keyword.setNumber(keyword.getNumber() + 1);
                            List<Article> articleList = keyword.getArticle();
                            articleList.add(articles.get(j));
                            keyword.setArticle(articleList);
                            keywords.add(keyword);
                            keywordDao.save(keyword);
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
        System.err.println(keywords.size());
        for (Keyword keyword : keywords) {
            System.err.println("=======" + keyword.getKeyword() + " " + keyword.getNumber() + "=======");
            for (Article article : keyword.getArticle()) {
                System.err.println("    " + article.getName() + " " + article.getTime());
                List<String> authors = article.getAuthor();
                if (authors != null) {
                    for (String author : authors) {
                        System.err.println("        " + author);
                    }
                }
            }
        }
        webDriver.close();
        webDriver.quit();
    }
}
