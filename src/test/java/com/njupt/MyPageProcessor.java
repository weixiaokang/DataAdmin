package com.njupt;

import com.njupt.model.bean.Article;
import com.njupt.model.bean.Keyword;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author weixk
 * @version Created time 17/4/5. Last-modified time 17/4/5.
 */
public class MyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        page.putField("page", page.getUrl());
        page.putField("keyword", page.getHtml().xpath("//label[@id='catalog_KEYWORD']/a/text()").toString());
        if (page.getResultItems().get("keyword") == null) {
            page.setSkip(true);
        }
        List<String> target = page.getHtml().xpath("//span[@class='name']/a/@href").all();
        page.addTargetRequests(target);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        Spider.create(new MyPageProcessor())
//                .setDownloader(new PhantomJSDownloader("phantomjs", "/Users/www1/IdeaProjects/DataAdmin/src/main/resources/static/crawl.js"))
//                //addUrl("http://navi.cnki.net/knavi/JournalDetail?pcode=CJFD&pykm=RJXB")
//                .addUrl("https://www.baidu.com")
//                .thread(5)
//                .run();
//        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//        desiredCapabilities.setJavascriptEnabled(true);
//        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Users/www1/Documents/phantomjs-2.1.1-macosx/bin/phantomjs");
//        System.setProperty("phantomjs.binary.path", "/Users/www1/Documents/phantomjs-2.1.1-macosx/bin/phantomjs");
        WebDriver webDriver = new PhantomJSDriver();
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
//            webDriver.get("http://navi.cnki.net/knavi/JournalDetail?pcode=CJFD&pykm=RJXB");
            webDriver.get("http://navi.cnki.net/knavi/JournalDetail?pcode=CJFD&pykm=JSJX");
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
                    String[] authors = authorElements.get(j).getText().split(";");
                    article.setAuthor(Arrays.asList(authors));
                    articles.add(article);
                }
                for (int j = 0; j < articles.size(); j++) {
                    webDriver.get(articles.get(j).getUrl());
                    try {
                        Thread.sleep(5 * 1000);
                        List<WebElement> keywordElements = webDriver.findElements(By.xpath("//a[contains(@onclick, 'kw')]"));
                        for (int k = 0; k < keywordElements.size(); k++) {
                            Keyword keyword = new Keyword();
                            keyword.setNumber(1);
                            keyword.setKeyword(keywordElements.get(k).getText().replace(";", ""));
                            List<Article> articleList = new ArrayList<>();
                            articleList.add(articles.get(j));
                            keyword.setArticle(articleList);
                            keywords.add(keyword);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println(keywords.size());
        for (Keyword keyword : keywords) {
            System.err.println("=======" + keyword.getKeyword() + " " + keyword.getNumber() + "=======");
            for (Article article : keyword.getArticle()) {
                System.err.println("    " + article.getName() + " " + article.getTime());
                for (String author : article.getAuthor()) {
                    System.err.println("        " + author);
                }
            }
        }
        webDriver.close();
        webDriver.quit();
    }
}

class MyConsolePipeline implements Pipeline {

    MyConsolePipeline() {

    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println(resultItems.getRequest().getUrl());
    }
}

class MyDownloader implements Downloader, Closeable {

    @Override
    public void close() throws IOException {

    }

    @Override
    public Page download(Request request, Task task) {
        return null;
    }

    @Override
    public void setThread(int i) {

    }
}