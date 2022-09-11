package io.zoemeow.dutapi;

import io.zoemeow.dutapi.objects.news.LinkItem;
import io.zoemeow.dutapi.objects.news.NewsGlobalItem;
import org.junit.jupiter.api.Test;

import java.util.List;

class NewsGlobalTest {
    @Test
    void getNews() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            System.out.println("==================================");
            System.out.println("Page " + page);

            List<NewsGlobalItem> newsList = News.getNewsGlobal(page);
            System.out.println("Item count: " + newsList.size());

            for (NewsGlobalItem newsItem : newsList) {
                System.out.println(newsItem.getDate());
                System.out.println(newsItem.getTitle());
                System.out.println(newsItem.getContentString());
                for (LinkItem linkItem : newsItem.getLinks()) {
                    System.out.println("Links:");
                    System.out.println(" - " + linkItem.getText());
                    System.out.println(" - " + linkItem.getUrl());
                    System.out.println(" - " + linkItem.getPosition());
                }
            }
            page += 1;
        }
    }
}