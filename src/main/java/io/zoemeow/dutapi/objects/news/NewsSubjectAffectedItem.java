package io.zoemeow.dutapi.objects.news;

import io.zoemeow.dutapi.objects.accounts.SubjectCodeItem;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsSubjectAffectedItem implements Serializable {
    private ArrayList<SubjectCodeItem> codeList = new ArrayList<>();
    private String subjectName = "";

    public NewsSubjectAffectedItem() { }

    public NewsSubjectAffectedItem(String subjectName) {
        this.subjectName = subjectName;
    }

    public ArrayList<SubjectCodeItem> getCodeList() {
        return codeList;
    }

    public void setCodeList(ArrayList<SubjectCodeItem> codeList) {
        this.codeList = codeList;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
