package io.zoemeow.dutapi;

import io.zoemeow.dutapi.customrequest.CustomRequest;
import io.zoemeow.dutapi.customrequest.CustomResponse;
import io.zoemeow.dutapi.objects.accounts.LessonItem;
import io.zoemeow.dutapi.objects.accounts.SubjectCodeItem;
import io.zoemeow.dutapi.objects.enums.LessonStatus;
import io.zoemeow.dutapi.objects.enums.NewsType;
import io.zoemeow.dutapi.objects.news.LinkItem;
import io.zoemeow.dutapi.objects.news.NewsGlobalItem;
import io.zoemeow.dutapi.objects.news.NewsSubjectAffectedItem;
import io.zoemeow.dutapi.objects.news.NewsSubjectItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public class News {
    private static ArrayList<NewsGlobalItem> getNews(NewsType newsType, Integer page) throws Exception {
        String url = "";

        switch (newsType) {
            case Global:
                url = String.format(Variables.URL_NEWS, "CTRTBSV", page);
                break;
            case Subject:
                url = String.format(Variables.URL_NEWS, "CTRTBGV", page);
                break;
        }

        CustomResponse response = CustomRequest.get(null, url);
        if (response.getReturnCode() < 200 || response.getReturnCode() >= 300)
            throw new Exception("Server was returned with code " + response.getReturnCode() + ".");

        // https://www.baeldung.com/java-with-jsoup

        Document webData = Jsoup.parse(response.getContentHtmlString());
        webData.outputSettings().charset(StandardCharsets.UTF_8);
        for (Element el : webData.getElementsByTag("br")) {
            el.remove();
        }

        // News General + News Subject
        Elements tbBox = webData.getElementsByClass("tbbox");

        ArrayList<NewsGlobalItem> newsList = new ArrayList<>();
        for (Element tb1 : tbBox) {
            NewsGlobalItem newsItem = new NewsGlobalItem();

            Element title = tb1.getElementsByClass("tbBoxCaption").get(0);
            String[] titleTemp = title.text().split(":", 2);
            Element content = tb1.getElementsByClass("tbBoxContent").get(0);

            if (titleTemp.length == 2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(titleTemp[0], formatter);
                LocalTime time = LocalTime.parse("00:00:00");
                LocalDateTime dateTime = date.atTime(time);
                newsItem.setDate(dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
                newsItem.setTitle(titleTemp[1].trim());
            } else newsItem.setTitle(title.text().trim());

            newsItem.setContent(content.html());
            newsItem.setContentString(content.wholeText());

            // Find links and set to item
            ArrayList<LinkItem> links = new ArrayList<>();
            int position = 0;
            String temp1 = content.wholeText();
            Elements temp2 = content.getElementsByTag("a");
            for (Element item : temp2) {
                if (temp1.contains(item.wholeText())) {
                    position += temp1.indexOf(item.wholeText());
                    LinkItem item1 = new LinkItem(
                            item.wholeText(),
                            item.attr("abs:href"),
                            position
                    );
                    links.add(item1);
                    position += item.wholeText().length();

                    // https://stackoverflow.com/questions/24220509/exception-when-replacing-brackets
                    String[] temp3 = temp1.split(Pattern.quote(item.wholeText()), 2);
                    if (temp3.length > 1)
                        temp1 = temp3[1];
                }
            }
            newsItem.setLinks(links);

            newsList.add(newsItem);
        }

        return newsList;
    }

    public static ArrayList<NewsGlobalItem> getNewsGlobal(Integer page) throws Exception {
        return getNews(NewsType.Global, page);
    }

    public static ArrayList<NewsSubjectItem> getNewsSubject(Integer page) throws Exception {
        ArrayList<NewsSubjectItem> result = new ArrayList<>();
        ArrayList<NewsGlobalItem> listTemp = getNews(NewsType.Subject, page);

        for (NewsGlobalItem item : listTemp) {
            NewsSubjectItem subjectItem = new NewsSubjectItem();

            // Add as like news global.
            subjectItem.setDate(item.getDate());
            subjectItem.setTitle(item.getTitle());
            subjectItem.setContent(item.getContent());
            subjectItem.setContentString(item.getContentString());
            subjectItem.setLinks(subjectItem.getLinks());

            // For title
            subjectItem.getAffectedClass().addAll(getAffectedClass(item.getTitle()));

            // For content. If found something, do work. If not, just ignore.
            if (subjectItem.getContent().contains("HỌC BÙ")) {
                subjectItem.setLessonStatus(LessonStatus.MakeUp);
                subjectItem.setAffectedDate(Utils.date2UnixTimestamp(
                        Utils.findFirstString(subjectItem.getContentString(), "\\d{2}[-|/]\\d{2}[-|/]\\d{4}")
                ));
                try {
                    subjectItem.setAffectedLesson(getLessonItem(
                            Objects.requireNonNull(Utils.findFirstString(subjectItem.getContentString().toLowerCase(), "tiết: .*[0-9],")).replace("tiết:", "").replace(",", "").trim()
                    ));
                } catch (Exception ignored) { }
                try {
                    subjectItem.setAffectedRoom(Objects.requireNonNull(Utils.findFirstString(subjectItem.getContentString().toLowerCase(), "phòng:.*")).replace("phòng:", "").replace(",", "").trim().toUpperCase());
                } catch (Exception ignored) { }
            } else if (subjectItem.getContent().contains("NGHỈ HỌC")) {
                subjectItem.setLessonStatus(LessonStatus.Leaving);
                subjectItem.setAffectedDate(Utils.date2UnixTimestamp(Utils.findFirstString(subjectItem.getContentString(), "\\d{2}[-|/]\\d{2}[-|/]\\d{4}")));
                try {
                    subjectItem.setAffectedLesson(getLessonItem(
                            Objects.requireNonNull(Utils.findFirstString(subjectItem.getContentString().toLowerCase(), "\\(tiết:.*[0-9]\\)")).replace("(tiết:", "").replace(")", "").trim()
                    ));
                } catch (Exception ignored) { }
            } else {
                subjectItem.setLessonStatus(LessonStatus.Unknown);
            }

            // Add to item.
            result.add(subjectItem);
        }

        return result;
    }

    private static LessonItem getLessonItem(String input) {
        if (input.contains("-")) {
            LessonItem item = new LessonItem();
            item.setStart(Integer.parseInt(input.split("-")[0]));
            item.setEnd(Integer.parseInt(input.split("-")[1]));
            return item;
        }
        else return null;
    }

    private static ArrayList<NewsSubjectAffectedItem> getAffectedClass(String input) {
        input = input.split(" thông báo đến lớp:")[1].trim();
        ArrayList<String> data1 = new ArrayList<>(Arrays.stream(input.split(" , ")).collect(Collectors.toList()));
        ArrayList<NewsSubjectAffectedItem> data2 = new ArrayList<>();

        for (String item: data1) {
            String itemSubjectName = item.substring(0, item.indexOf("[")).trim();
            String itemClass = item.substring(item.indexOf("[") + 1, item.indexOf("]")).toLowerCase().replace("nh", "");
            SubjectCodeItem codeItem = new SubjectCodeItem(
                    itemClass.split("\\.")[0],
                    itemClass.split("\\.")[1]
            );

            if (data2.stream().noneMatch(p -> Objects.equals(p.getSubjectName(), itemSubjectName))) {
                NewsSubjectAffectedItem item2 = new NewsSubjectAffectedItem();
                item2.setSubjectName(itemSubjectName);
                item2.getCodeList().add(codeItem);
                data2.add(item2);
            }
            else {
                Optional<NewsSubjectAffectedItem> tempdata = data2.stream().filter(p -> Objects.equals(p.getSubjectName(), itemSubjectName)).findFirst();
                if (tempdata.isPresent()) {
                    NewsSubjectAffectedItem temp = tempdata.get();
                    temp.getCodeList().add(codeItem);
                }
            }
        }

        return data2;
    }
}
