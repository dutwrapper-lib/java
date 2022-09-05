package io.zoemeow.dutapi.objects.accounts;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Details in http://daotao.dut.udn.vn/download2/Guide_Dangkyhoc.pdf, page 28
public class SubjectCodeItem implements Serializable {
    // Area 1
    private Integer subjectId = 0;
    // Area 2
    private Integer schoolYearId = 0;
    // Area 3
    private Integer studentYearId = 0;
    // Area 4
    private String classId = "";

    public SubjectCodeItem() {

    }

    public SubjectCodeItem(Integer studentYearId, String classId) {
        this.studentYearId = studentYearId;
        this.classId = classId;
    }

    public SubjectCodeItem(Integer subjectId, Integer schoolYearId, Integer studentYearId, String classId) {
        this.subjectId = subjectId;
        this.schoolYearId = schoolYearId;
        this.studentYearId = studentYearId;
        this.classId = classId;
    }

    public SubjectCodeItem(String input) {
        if (input.split("\\.").length == 4) {
            this.subjectId = Integer.parseInt(input.split("\\.")[0]);
            this.schoolYearId = Integer.parseInt(input.split("\\.")[1]);
            this.studentYearId = Integer.parseInt(input.split("\\.")[2]);
            this.classId = input.split("\\.")[3];
        } else if (input.split("\\.").length == 2) {
            this.studentYearId = Integer.parseInt(input.split("\\.")[0]);
            this.classId = input.split("\\.")[1];
        }
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(Integer schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    public Integer getStudentYearId() {
        return studentYearId;
    }

    public void setStudentYearId(Integer studentYearId) {
        this.studentYearId = studentYearId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(Boolean twoLastDigit) {
        if (twoLastDigit)
            return String.format("%02d.%s", studentYearId, classId);
        else return String.format("%02d.%02d.%02d.%s", subjectId, schoolYearId, studentYearId, classId);
    }

    public Boolean equalsTwoDigits(SubjectCodeItem codeItem) {
        return Objects.equals(this.studentYearId, codeItem.studentYearId) &&
                Objects.equals(this.classId, codeItem.classId);
    }

    public Boolean equals(SubjectCodeItem codeItem) {
        return Objects.equals(this.subjectId, codeItem.subjectId) &&
                Objects.equals(this.studentYearId, codeItem.studentYearId) &&
                this.equalsTwoDigits(codeItem);
    }
}
