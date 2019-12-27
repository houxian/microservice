package com.imooc.course.dto;

import java.io.Serializable;

public class CourseDTO implements Serializable {


    private  int id;
    private String title;
    private String desriptopn;
    private String trcher;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesriptopn() {
        return desriptopn;
    }

    public void setDesriptopn(String desriptopn) {
        this.desriptopn = desriptopn;
    }

    public String getTrcher() {
        return trcher;
    }

    public void setTrcher(String trcher) {
        this.trcher = trcher;
    }
}
