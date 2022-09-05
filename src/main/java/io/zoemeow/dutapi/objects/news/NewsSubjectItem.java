package io.zoemeow.dutapi.objects.news;

import io.zoemeow.dutapi.objects.accounts.LessonItem;
import io.zoemeow.dutapi.objects.enums.LessonStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsSubjectItem extends NewsGlobalItem implements Serializable {
    private ArrayList<NewsSubjectAffectedItem> affectedClass = new ArrayList<>();
    private Long affectedDate = 0L;
    private LessonStatus lessonStatus = LessonStatus.Unknown;
    private LessonItem affectedLesson;
    private String affectedRoom;

    public Long getAffectedDate() {
        return affectedDate;
    }

    public void setAffectedDate(Long affectedDate) {
        this.affectedDate = affectedDate;
    }

    public LessonStatus getLessonStatus() {
        return lessonStatus;
    }

    public void setLessonStatus(LessonStatus lessonStatus) {
        this.lessonStatus = lessonStatus;
    }

    public LessonItem getAffectedLesson() {
        return affectedLesson;
    }

    public void setAffectedLesson(LessonItem affectedLesson) {
        this.affectedLesson = affectedLesson;
    }

    public String getAffectedRoom() {
        return affectedRoom;
    }

    public void setAffectedRoom(String affectedRoom) {
        this.affectedRoom = affectedRoom;
    }

    public ArrayList<NewsSubjectAffectedItem> getAffectedClass() {
        return affectedClass;
    }

    public void setAffectedClass(ArrayList<NewsSubjectAffectedItem> affectedClass) {
        this.affectedClass = affectedClass;
    }

    public NewsSubjectItem(ArrayList<NewsSubjectAffectedItem> affectedClass, Long affectedDate, LessonStatus lessonStatus, LessonItem affectedLesson, String affectedRoom) {
        this.affectedClass = affectedClass;
        this.affectedDate = affectedDate;
        this.lessonStatus = lessonStatus;
        this.affectedLesson = affectedLesson;
        this.affectedRoom = affectedRoom;
    }

    public NewsSubjectItem() { }
}
